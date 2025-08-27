import { useMutation, useQueryClient } from "@tanstack/react-query";
import {
    Button,
    Calendar,
    Col,
    Drawer,
    Form,
    Row,
    Select,
    TimePicker
} from "antd";
import { Dayjs } from "dayjs";
import { useState } from "react";
import { toast } from "react-toastify";
import type { MyTrainingSessionRequest } from "../../utils/types/user-authenticated/MyTrainingSession";
import { createMyTrainingSession } from "../../utils/api";
import { formatDate } from "../../misc/formatDate";

export default function TrainingSessionPage() {
    const queryClient = useQueryClient()
    const [selectedDate, setSelectedDate] = useState<Dayjs | null>(null)
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
        setSelectedDate(value)
        setModalOpen(true)
        form.resetFields()
    }

    const handleSubmit = (values: any) => {
        if (!selectedDate || !values.timeRange) {
            toast.warning("Please select a date and time range")
            return
        }

        const [startTime, endTime] = values.timeRange

        const beginningOfSession = selectedDate
            .hour(startTime.hour())
            .minute(startTime.minute())
            .second(0)
            .millisecond(0)
            .format("YYYY-MM-DDTHH:mm:ss")

        const endOfSession = selectedDate
            .hour(endTime.hour())
            .minute(endTime.minute())
            .second(0)
            .millisecond(0)
            .format("YYYY-MM-DDTHH:mm:ss")

        const request: MyTrainingSessionRequest = {
            trainingType: values.trainingType,
            trainingLevel: values.trainingLevel,
            beginningOfSession,
            endOfSession
        }
        createSessionMutation.mutate(request)
    }

    return (
        <div
            style={{
                maxWidth: 900,
                margin: "0 auto",
                padding: 20
            }}
        >
            <h2>
                Create a training session
            </h2>
            <Calendar fullscreen={false} onSelect={handleDateSelect} />
            <Drawer
                title={`Create session for ${formatDate(selectedDate?.format("YYYY-MM-DD") || "")}`}
                open={modalOpen}
                onClose={() => setModalOpen(false)}
                size="large"
                placement="bottom"
            >
                <Row justify="center">
                    <Col span={8}>
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
                                label="Training type"
                                rules={[{ required: true, message: "Please select training type" }]}
                            >
                                <Select
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
                                label="Training level"
                                rules={[{ required: true, message: "Please select training level" }]}
                            >
                                <Select
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
                                label="Time range"
                                rules={[{ required: true, message: "Please select time range" }]}
                            >
                                <TimePicker.RangePicker format="HH:mm" />
                            </Form.Item>
                            <Form.Item>
                                <Button
                                    type="primary"
                                    htmlType="submit"
                                    loading={createSessionMutation.isPending}
                                >
                                    Create session
                                </Button>
                            </Form.Item>
                        </Form>
                    </Col>
                </Row>
            </Drawer>
        </div>
    )
}