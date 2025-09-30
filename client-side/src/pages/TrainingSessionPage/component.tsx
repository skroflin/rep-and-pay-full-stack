import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import {
    Button,
    Calendar,
    Col,
    Descriptions,
    Drawer,
    Flex,
    Form,
    List,
    Row,
    Select,
    Spin,
    Tag,
    TimePicker,
    Typography
} from "antd";
import dayjs, { Dayjs } from "dayjs";
import { useState } from "react";
import { toast } from "react-toastify";
import type { MyTrainingSessionRequest } from "../../utils/types/user-authenticated/MyTrainingSession";
import { createMyTrainingSession, getAvailableTrainingSessions } from "../../utils/api";
import { formatDate } from "../../misc/formatDate";
import {
    ClockCircleFilled,
    ClockCircleOutlined,
    FireOutlined,
    FormOutlined,
    LoadingOutlined,
    PlusCircleFilled,
    SettingOutlined,
    SnippetsOutlined
} from "@ant-design/icons";
import type { TrainingSessionResponse } from "../../utils/types/TrainingSession";
import type { AxiosError } from "axios";

export default function TrainingSessionPage() {
    const queryClient = useQueryClient()
    const [selectedDate, setSelectedDate] = useState<string>(dayjs().format("YYYY-MM-DD"))
    const [modalOpen, setModalOpen] = useState(false)
    const [form] = Form.useForm()

    const createSessionMutation = useMutation({
        mutationFn: (req: MyTrainingSessionRequest) => createMyTrainingSession(req),
        onSuccess: () => {
            toast.success("Your training session is created!", { position: "top-center" })
            queryClient.invalidateQueries({ queryKey: ["my-training-session"] })
            setModalOpen(false)
            form.resetFields()
        },
        onError: (err: AxiosError) => {
            if (err.response?.status === 400) {
                toast.error(`There is already a session booked for this time range on ${formatDate(selectedDate)}!`, { position: "top-center" })
            } else {
                toast.error("Failed to create a training session!", { position: "top-center" })
            }
        }
    })

    const handleDateSelect = (value: Dayjs) => {
        setSelectedDate(value.format("YYYY-MM-DD"))
        setModalOpen(true)
        form.resetFields()
    }

    const handleSubmit = (values: any) => {
        if (!selectedDate || !values.timeRange) {
            toast.warning("Please select a date and time range!", { position: "top-center" })
            return
        }

        const [startTime, endTime] = values.timeRange

        if (endTime.isSame(startTime) || endTime.isBefore(startTime)) {
            toast.warning("End time must be after start time and not equal to start time!", { position: "top-center" })
            return
        }

        const newStart = dayjs(selectedDate)
            .hour(startTime.hour())
            .minute(startTime.minute())
            .second(0).millisecond(0)

        const newEnd = dayjs(selectedDate)
            .hour(endTime.hour())
            .minute(endTime.minute())
            .second(0)
            .millisecond(0)

        const hasOverlap = (sessions || []).some(session => {
            const existingStart = dayjs(session.beginningOfSession)
            const existingEnd = dayjs(session.endOfSession)
            return newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart)
        })

        if (hasOverlap) {
            const overlappingSessionIndex = (sessions || []).findIndex(session => {
                const existingStart = dayjs(session.beginningOfSession)
                const existingEnd = dayjs(session.endOfSession)
                return newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart)
            })

            if (overlappingSessionIndex !== -1 && sessions) {
                const overlappingSession = sessions[overlappingSessionIndex]
                const coachName = `${overlappingSession.trainerFirstName} ${overlappingSession.trainerLastName}`
                toast.warning(
                    `This session overlaps with an existing session by coach ${coachName}!`,
                    { position: "top-center" }
                )
            } else {
                toast.warning(`This session overlaps with an existing session!`, { position: "top-center" })
            }
            return

            // toast.warning(`This session overlaps with an existing session!`, { position: "top-center" })
            // return;
        }

        const beginningOfSession = dayjs(selectedDate)
            .hour(startTime.hour())
            .minute(startTime.minute())
            .second(0)
            .millisecond(0)
            .format("YYYY-MM-DDTHH:mm:ss")

        const endOfSession = dayjs(selectedDate)
            .hour(endTime.hour())
            .minute(endTime.minute())
            .second(0)
            .millisecond(0)
            .format("YYYY-MM-DDTHH:mm:ss")

        const request: MyTrainingSessionRequest = {
            trainingType: values.trainingType,
            trainingLevel: values.trainingLevel,
            beginningOfSession,
            endOfSession,
            alreadyBooked: values.alreadyBooked
        }
        createSessionMutation.mutate(request)
    }

    const { data: sessions, isLoading } = useQuery<TrainingSessionResponse[]>({
        queryKey: ["my-training-session", selectedDate],
        queryFn: () => getAvailableTrainingSessions(selectedDate),
        enabled: !!selectedDate
    })

    const { Title, Text } = Typography

    return (
        <div
            style={{
                maxWidth: 900,
                margin: "0 auto",
                padding: 20
            }}
        >
            <h1>
                Create a training session <FormOutlined />
            </h1>
            <Calendar fullscreen={false} onSelect={handleDateSelect} />
            <Drawer
                title={
                    <Title
                        level={3}
                        style={{
                            textAlign: "center"
                        }}
                    >
                        {`Create session for ${formatDate(dayjs(selectedDate).format("YYYY-MM-DD"))}`}
                        <FormOutlined style={{ marginLeft: 10 }} />
                    </Title>
                }
                open={modalOpen}
                onClose={() => setModalOpen(false)}
                width={650}
                placement="right"
            >
                <Row justify="center">
                    <Col span={10}>
                        <Form
                            form={form}
                            layout="vertical"
                            onFinish={handleSubmit}
                            style={{
                                width: "100%"
                            }}
                        >
                            <Form.Item
                                name="trainingType"
                                label={
                                    <Text
                                        strong
                                    >
                                        Training type
                                    </Text>
                                }
                                rules={[{ required: true, message: "Please select training type" }]}
                            >
                                <Select
                                    prefix={<SettingOutlined />}
                                    placeholder="Select training type"
                                    options={[
                                        { value: "push", label: "Push" },
                                        { value: "pull", label: "Pull" },
                                        { value: "legs", label: "Legs" },
                                        { value: "crossfit", label: "Crossfit" },
                                        { value: "conditioning", label: "Conditioning" },
                                        { value: "yoga", label: "Yoga" },
                                        { value: "weightlifting", label: "Weightlifting" }
                                    ]}
                                />
                            </Form.Item>
                            <Form.Item
                                name="trainingLevel"
                                label={
                                    <Text
                                        strong
                                    >
                                        Training level
                                    </Text>
                                }
                                rules={[{ required: true, message: "Please select training level" }]}
                            >
                                <Select
                                    prefix={<FireOutlined />}
                                    placeholder="Select training level"
                                    options={[
                                        { value: "beginner", label: "Beginner" },
                                        { value: "intermediate", label: "Intermediate" },
                                        { value: "advanced", label: "Advanced" }
                                    ]}
                                />
                            </Form.Item>
                            <Form.Item
                                name="timeRange"
                                label={
                                    <Text
                                        strong
                                    >
                                        Time range
                                    </Text>
                                }
                                rules={[{ required: true, message: "Please select time range" }]}
                            >
                                <TimePicker.RangePicker format="HH:mm" />
                            </Form.Item>
                            <Form.Item
                                style={{
                                    display: "flex",
                                    justifyContent: "center",
                                    alignItems: "center"
                                }}
                            >
                                <Button
                                    type="primary"
                                    htmlType="submit"
                                    loading={createSessionMutation.isPending}
                                    icon={<PlusCircleFilled />}
                                >
                                    Create session
                                </Button>
                            </Form.Item>
                        </Form>
                        <Title level={4} style={{ textAlign: "center" }}>
                            Existing sessions for {formatDate(dayjs(selectedDate).format("YYYY-MM-DD"))} <SnippetsOutlined />
                        </Title>
                        {isLoading ? (
                            <Flex align="center" justify="center" vertical>
                                <Spin
                                    indicator={<LoadingOutlined style={{ color: "black", fontSize: 48 }} spin />}
                                    style={{ fontSize: 64 }}
                                />
                                <Title level={4} style={{ marginTop: 16 }}>Loading dates...</Title>
                            </Flex>
                        ) : sessions && sessions.length > 0 ? (
                            <List
                                pagination={{ pageSize: 1 }}
                                bordered
                                dataSource={sessions}
                                renderItem={(session) => (
                                    <List.Item>
                                        <Descriptions column={1} size="small" style={{ width: "100%" }}>
                                            <Descriptions.Item label="Coach">
                                                <Text strong>{session.trainerFirstName} {session.trainerLastName}</Text>
                                            </Descriptions.Item>
                                            <Descriptions.Item label="Start">
                                                <Tag icon={<ClockCircleOutlined />}>{dayjs(session.beginningOfSession).format("HH:mm")}</Tag>
                                            </Descriptions.Item>
                                            <Descriptions.Item label="End">
                                                <Tag icon={<ClockCircleFilled />}>{dayjs(session.endOfSession).format("HH:mm")}</Tag>
                                            </Descriptions.Item>
                                            <Descriptions.Item label="Type">
                                                <Tag color="blue" icon={<SettingOutlined />}>{session.trainingType.toUpperCase()}</Tag>
                                            </Descriptions.Item>
                                            <Descriptions.Item label="Level">
                                                <Tag color="geekblue" icon={<FireOutlined />}>{session.trainingLevel.toUpperCase()}</Tag>
                                            </Descriptions.Item>
                                        </Descriptions>
                                    </List.Item>
                                )}
                            />
                        ) : (
                            <Text>No sessions available for this date.</Text>
                        )}
                    </Col>
                </Row>
            </Drawer>
        </div>
    )
}