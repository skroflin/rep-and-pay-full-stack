import { useMutation, useQueryClient } from "@tanstack/react-query";
import {
    Button,
    Calendar,
    Form,
    Modal,
    Select,
    TimePicker
} from "antd";
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

        const [startTime, endTime] = timeRange

        const beginningOfSession = selectedDate
            .hour(startTime.hour())
            .minute(startTime.minute())
            .format("YYYY-MM-DDTHH:mm:ss")

        const endOfSession = selectedDate
            .hour(endTime.hour())
            .minute(endTime.minute())
            .format("YYYY-MM-DDTHH:mm:ss")

        const request: MyTrainingSessionRequest = {
            trainingType: values.trainingType,
            trainingLevel: values.trainingLevel,
            beginningOfSession,
            endOfSession
        }
        console.log(request)
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
                                { value: "PUSH", label: "Push" },
                                { value: "PULL", label: "Pull" },
                                { value: "LEGS", label: "Legs" },
                                { value: "CROSSFIT", label: "Crossfit" },
                                { value: "CONDITIONING", label: "Conditioning" },
                                { value: "YOGA", label: "Yoga" },
                                { value: "WEIGHTLIFTING", label: "Weightlifting" }
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
                                { value: "BEGINNER", label: "Beginner" },
                                { value: "INTERMEDIATE", label: "Intermediate" },
                                { value: "ADVANCED", label: "Advanced" }
                            ]}
                        />
                    </Form.Item>
                    <Form.Item
                        label="Time range"
                        required
                    >
                        <TimePicker.RangePicker
                            format="HH:mm"
                            value={timeRange}
                            onChange={(values) => {
                                if (values) {
                                    setTimeRange(values as [Dayjs, Dayjs])
                                } else {
                                    setTimeRange(null)
                                }
                            }}
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