import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import {
    Button,
    Calendar,
    Col,
    Descriptions,
    Drawer,
    Form,
    List,
    Row,
    Select,
    Spin,
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
    PlusCircleFilled,
    SettingFilled,
    SettingOutlined,
    SnippetsOutlined
} from "@ant-design/icons";
import type { TrainingSessionResponse } from "../../utils/types/TrainingSession";

export default function TrainingSessionPage() {
    const queryClient = useQueryClient()
    const [selectedDate, setSelectedDate] = useState<string>(dayjs().format("YYYY-MM-DD"))
    const [modalOpen, setModalOpen] = useState(false)
    const [form] = Form.useForm()

    const createSessionMutation = useMutation({
        mutationFn: (req: MyTrainingSessionRequest) => createMyTrainingSession(req),
        onSuccess: () => {
            toast.success("Your training session is created!")
            queryClient.invalidateQueries({ queryKey: ["my-training-session"] })
            setModalOpen(false)
            form.resetFields()
        },
        onError: () => toast.error("Failed to create your training session")
    })

    const handleDateSelect = (value: Dayjs) => {
        setSelectedDate(value.format("YYYY-MM-DD"))
        setModalOpen(true)
        form.resetFields()
    }

    const handleSubmit = (values: any) => {
        if (!selectedDate || !values.timeRange) {
            toast.warning("Please select a date and time range")
            return
        }

        const [startTime, endTime] = values.timeRange

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
            alreadyBooked
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
                                        { value: "crossfir", label: "Crossfit" },
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
                            <Spin />
                        ) : sessions && sessions.length > 0 ? (
                            <List
                                bordered
                                dataSource={sessions}
                                renderItem={(session) => (
                                    <List.Item>
                                        <Descriptions column={1} size="small" style={{ width: "100%" }}>
                                            <Descriptions.Item label="Start">
                                                <Text><ClockCircleOutlined /> {dayjs(session.beginningOfSession).format("HH:mm")}</Text>
                                            </Descriptions.Item>
                                            <Descriptions.Item label="End">
                                                <Text><ClockCircleFilled /> {dayjs(session.endOfSession).format("HH:mm")}</Text>
                                            </Descriptions.Item>
                                            <Descriptions.Item label="Type">
                                                <Text style={{ textTransform: "capitalize" }}><SettingOutlined /> {session.trainingType}</Text>
                                            </Descriptions.Item>
                                            <Descriptions.Item label="Level">
                                                <Text style={{ textTransform: "capitalize" }}><SettingFilled /> {session.trainingLevel}</Text>
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