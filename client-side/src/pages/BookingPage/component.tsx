import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { Button, Calendar, List, Spin } from "antd";
import { useState } from "react";
import { toast } from "react-toastify";
import { createMyBooking, getAvailableTrainingSessions } from "../../utils/api";
import dayjs, { Dayjs } from "dayjs";
import type { BookingRequest } from "../../utils/types/Booking";

export default function BookingPage() {
    const queryClient = useQueryClient()
    const [selectedDate, setSelectedDate] = useState<string>(dayjs().format("DD.MM.YYYY"))

    const { data: sessions, isLoading } = useQuery({
        queryKey: ["available-sessions", selectedDate],
        queryFn: () => getAvailableTrainingSessions(selectedDate)
    })

    const bookingMutation = useMutation({
        mutationFn: (req: BookingRequest) => createMyBooking(req),
        onSuccess: () => {
            toast.success("Booking request sent!")
            queryClient.invalidateQueries({ queryKey: ["available-sessions", selectedDate ]})
        },
        onError: () => toast.error("Failed to create a booking!"),
    })

    const handleDataChange = (value: Dayjs) => {
        const dateStr = value.format("DD.MM.YYYY")
        setSelectedDate(dateStr)
        toast.info(`Date selected: ${dateStr}!`)
    }

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
            <h2>Available sessions for {selectedDate}</h2>

            {isLoading ? (
                <Spin/>
            ): (
                <List
                    bordered
                    dataSource={sessions || []}
                    renderItem={(session: any) => (
                        <List.Item
                            actions={[
                                <Button
                                    type="primary"
                                    onClick={() => 
                                        bookingMutation.mutate({ trainingSessionId })
                                    }
                                    loading={bookingMutation.isPending}
                                >
                                    Book
                                </Button>
                            ]}
                        >
                            <List.Item.Meta
                                title={`${session.startTime} - ${session.endTime}`}
                            />
                        </List.Item>
                    )}
                >

                </List>
            )}
        </div>
    )
}