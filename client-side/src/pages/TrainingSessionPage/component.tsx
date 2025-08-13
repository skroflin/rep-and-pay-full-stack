import { useMutation, useQueryClient } from "@tanstack/react-query";
import { Button, Calendar, Form, message, Modal, Select, TimePicker } from "antd";
import { Dayjs } from "dayjs";
import { useState } from "react";
import { toast } from "react-toastify";
import type { MyTrainingSessionRequest } from "../../utils/types/user-authenticated/MyTrainingSession";
import { createMyTrainingSession } from "../../utils/api";

export default function TrainingSessionPage() {
    const queryClient = useQueryClient()
    const [selectedDate, setSelectedDate] = useState<Dayjs | null>(null)
    const [timeRange, setTimeRange] = useState<[Dayjs, Dayjs] | null>(null)
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
    }

    const handleSubmit = (values: any) => {
        if (!selectedDate || !timeRange) {
            toast.warning("Please select a date and time range")
            return
        }

        const [trainingDate] = timeRange

        const dateTime = selectedDate
            .hour(trainingDate.hour())
            .minute(trainingDate.minute())
            .toDate()

        const request: MyTrainingSessionRequest = {
            trainingType: values.trainingType,
            trainingLevel: values.trainingLevel,
            dateTime
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
            <Modal
                title={`Create session for ${selectedDate?.format("YYYY-MM-DD")}`}
                open={modalOpen}
                onCancel={() => setModalOpen(false)}
            >
                <Form
                    form={form}
                    layout="vertical"
                    onFinish={handleSubmit}
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
                                { value: "crossfit", label: "Crossfit" },
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
                        label="Time range"
                        required
                    >
                        <TimePicker
                            format="HH:mm"
                            minuteStep={15}
                            onChange={(values) => setTimeRange(values as [Dayjs, Dayjs])}
                        />
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
            </Modal>
        </div>
    )
}