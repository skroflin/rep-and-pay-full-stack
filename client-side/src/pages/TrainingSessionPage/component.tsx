import { useMutation, useQueryClient } from "@tanstack/react-query";
import { Calendar, Form } from "antd";
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
                display: "block",
                width: 700,
                padding: 30
            }}
        >
            <Calendar
                onChange={(value) => {
                    toast.success(`You selected ${value.format('DD.MM.YYYY')}`)
                }}
            />
        </div>
    )
}