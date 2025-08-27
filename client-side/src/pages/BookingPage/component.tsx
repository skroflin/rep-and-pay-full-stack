import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import {
    Button,
    Calendar,
    List,
    Spin,
    Typography
} from "antd";
import { useState } from "react";
import { toast } from "react-toastify";
import {
    createMyBooking,
    getAvailableTrainingSessions
} from "../../utils/api";
import dayjs, { Dayjs } from "dayjs";
import type { MyBookingRequest } from "../../utils/types/user-authenticated/MyBooking";
import type { TrainingSessionResponse } from "../../utils/types/TrainingSession";
import { formatDate } from "../../misc/formatDate";
import { MehFilled, OrderedListOutlined, PlusCircleFilled } from "@ant-design/icons";

export default function BookingPage() {
    const queryClient = useQueryClient()
    const [selectedDate, setSelectedDate] = useState<string>(dayjs().format("YYYY-MM-DD"))
    const [pendingSession, setPendingSession] = useState<string[]>([])

    const { data: sessions, isLoading } = useQuery<TrainingSessionResponse[]>({
        queryKey: ["available-sessions", selectedDate],
        queryFn: () => getAvailableTrainingSessions(selectedDate)
    })

    const bookingMutation = useMutation({
        mutationFn: (req: MyBookingRequest) => createMyBooking(req),
        onSuccess: () => {
            toast.success("Booking request sent!")
            queryClient.invalidateQueries({ queryKey: ["available-sessions", selectedDate] })
        },
        onError: () => toast.error("Failed to create a booking!"),
    })

    const handleDataChange = (value: Dayjs) => {
        const dateStr = value.format("YYYY-MM-DD")
        setSelectedDate(dateStr)
    }

    const handleBooking = (session: TrainingSessionResponse) => {

        setPendingSession(prev => [...prev, session.id])
        bookingMutation.mutate({
            trainingSessionId: session.id
        },
            {
                onError: () => {
                    setPendingSession(prev => prev.filter(id => id !== session.id))
                }
            })
    }

    const { Text } = Typography

    return (
        <div
            style={{
                display: "block",
                maxWidth: 800,
                padding: 20,
                margin: "0 auto"
            }}
        >
            <Calendar
                fullscreen={false}
                onChange={handleDataChange}
            />
            <h2><OrderedListOutlined /> Available sessions for {formatDate(selectedDate)}</h2>

            {isLoading ? (
                <Spin />
            ) : (
                <List
                    bordered
                    dataSource={sessions || []}
                    renderItem={(session) => (
                        <List.Item
                            actions={[
                                <Button
                                    type="primary"
                                    onClick={() => handleBooking(session)}
                                    loading={bookingMutation.isPending}
                                    disabled={pendingSession.includes(session.id)}
                                    icon={<PlusCircleFilled />}
                                >
                                    Book
                                </Button>
                            ]}
                        >
                            <List.Item.Meta
                                style={{ textTransform: "capitalize" }}
                                title={`${session.trainingType}`}
                                description={
                                    `${session.trainingLevel} by 
                                    ${session.trainerFirstName} ${session.trainerLastName}`
                                }
                            />
                        </List.Item>
                    )}
                    pagination={{ pageSize: 1 }}
                    locale={{
                        emptyText: (
                            <Text strong>No sessions available for {formatDate(selectedDate)} <MehFilled /></Text>
                        )
                    }}
                >
                </List>
            )}
        </div>
    )
}