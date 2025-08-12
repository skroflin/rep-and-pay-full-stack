import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import type { TrainerBookingResponse } from "../../utils/types/Booking";
import { getTrainerBookings, updateBookingStatus } from "../../utils/api";
import dayjs from "dayjs";
import { Button, Calendar, List, Modal, Space, Spin, Typography } from "antd";

export default function NotificationsPage() {
    const { Text } = Typography
    const queryClient = useQueryClient()
    const [selectedBooking, setSelectedBooking] = useState<TrainerBookingResponse | null>(null)
    const [selectedDate, setSelectedDate] = useState(dayjs())

    const { data: bookings, isLoading } = useQuery<TrainerBookingResponse[]>({
        queryKey: ["trainer-bookings"],
        queryFn: getTrainerBookings
    })

    const updateStatusMutation = useMutation({
        mutationFn: ({ bookingId, bookingStatus }: { bookingId: string, bookingStatus: "APPROVED" | "REJECTED" }) =>
            updateBookingStatus(bookingId, { bookingStatus }),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["trainer-bookings"] })
            setSelectedBooking(null)
        }
    })

    const handleDecision = (bookingStatus: "APPROVED" | "REJECTED") => {
        if (selectedBooking) {
            updateStatusMutation.mutate({ bookingId: selectedBooking.bookingId, bookingStatus })
        }
    }

    const bookingsForDate = bookings?.filter((b) =>
        dayjs(b.reservationTime).isSame(selectedDate, "day")
    ) || []

    const dateCellRender = (value: dayjs.Dayjs) => {
        const hasPending = bookings?.some(
            (b) => 
                b.bookingStatus === "PENDING" &&
                dayjs(b.reservationTime).isSame(value, "day")
        )
        return hasPending ? <div style={{color: "red"}}>-</div> : null
    }

    return (
        <>
            <Calendar
                onSelect={(date) => setSelectedDate(date.startOf("day"))}
                fullCellRender={dateCellRender}
                fullscreen={false}
            />

            {isLoading && <Spin />}

            {bookingsForDate.length > 0 && (
                <>
                    <Typography.Title
                        level={4}
                        style={{ marginTop: 16 }}
                    >
                        Bookings for {selectedDate.format("YYYY-MM-DD")}
                    </Typography.Title>
                    <List
                        dataSource={bookingsForDate}
                        renderItem={(booking) => (
                            <List.Item
                                key={booking.bookingId}
                                actions={[
                                    booking.bookingStatus === "PENDING" && (
                                        <Button
                                            type="primary"
                                            onClick={() => setSelectedBooking(booking)}
                                        >
                                            Review
                                        </Button>
                                    ),
                                ]}
                            >
                                <Space direction="vertical">
                                    <Text strong>{booking.userFirstName} {booking.userLastName}</Text>
                                    <Text>{booking.trainingType}</Text>
                                    <Text type="secondary">
                                        {dayjs(booking.reservationTime).format("HH:mm")} - {dayjs(booking.endOfReservationTime).format("HH:mm")}
                                    </Text>
                                    <Text>Status: {booking.bookingStatus}</Text>
                                </Space>
                            </List.Item>
                        )}
                    />
                </>
            )}

            <Modal
                title="Review Booking Request"
                open={!!selectedBooking}
                onCancel={() => setSelectedBooking(null)}
            >

            </Modal>
        </>
    )
}