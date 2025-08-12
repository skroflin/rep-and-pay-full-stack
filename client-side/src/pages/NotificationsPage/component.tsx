import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import type { TrainerBookingResponse } from "../../utils/types/Booking";
import { getTrainerBookings, updateBookingStatus } from "../../utils/api";
import dayjs from "dayjs";
import { Button, Calendar, Modal, Typography } from "antd";

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

    const bookingsForDate = bookings?.filter(
        (b) =>
            b.bookingStatus === "PENDING" &&
            dayjs(b.reservationTime).isSame(selectedDate, "day")
    ) || []

    const dateCellRender = (value: dayjs.Dayjs) => {
        if (!bookings) return null
        const listData = bookings.filter(
            (booking) =>
                booking.bookingStatus === "PENDING" &&
                dayjs(booking.reservationTime).isSame(value, "day")
        )

        return (
            <ul className="events">
                {listData?.map((item) => (
                    <li
                        key={item.bookingId}
                        style={{ cursor: "pointer" }}
                        onClick={() => setSelectedBooking(item)}
                    >
                        <Text strong>
                            {item.userFirstName} {item.userLastName}
                        </Text>
                        - <Text>
                            {item.trainingType}
                        </Text>
                    </li>
                ))}
            </ul>
        )
    }

    return (
        <>
            <Calendar
                onSelect={(date) => setSelectedDate(date)}
                cellRender={dateCellRender}
            />

            <Modal
                title="Review Booking Request"
                open={!!selectedBooking}
                onCancel={() => setSelectedBooking(null)}
                footer={[
                    <Button
                        key="reject"
                        danger
                        onClick={() => handleDecision("REJECTED")}
                        loading={updateStatusMutation.isPending}
                    >
                        Reject
                    </Button>,
                    <Button
                        key="approve"
                        type="primary"
                        onClick={() => handleDecision("APPROVED")}
                        loading={updateStatusMutation.isPending}
                    >
                        Approve
                    </Button>
                ]}
            >
                {selectedBooking && (
                    <div>
                        <div>
                            <Text strong>
                                User:
                            </Text>
                            <Text>
                                {selectedBooking.userFirstName} {selectedBooking.userLastName}
                            </Text>
                        </div>
                        <div>
                            <Text strong>
                                From:
                            </Text>
                            <Text>
                                {dayjs(selectedBooking.reservationTime).format("YYYY-MM-DD HH:mm")}
                            </Text>
                        </div>
                        <div>
                            <Text strong>
                                To:
                            </Text>
                            <Text>
                                {dayjs(selectedBooking.endOfReservationTime).format("YYYY-MM-DD HH:mm")}
                            </Text>
                        </div>
                    </div>
                )}
            </Modal>
        </>
    )
}