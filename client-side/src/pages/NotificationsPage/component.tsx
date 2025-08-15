import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import type { TrainerBookingResponse } from "../../utils/types/Booking";
import { getTrainerBookings, updateBookingStatus } from "../../utils/api";
import dayjs from "dayjs";
import { Badge, Calendar, Spin } from "antd";
import { CalendarOutlined } from "@ant-design/icons";
import BookingReviewModal from "./BookingReviewModal";

export default function NotificationsPage() {
    const queryClient = useQueryClient()
    const [selectedBooking, setSelectedBooking] = useState<TrainerBookingResponse | null>(null)

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

    const onSelectDate = (date: dayjs.Dayjs) => {
        const bookingsForDate = bookings?.filter((b) => 
            dayjs(b.beginningOfSession).isSame(date, "day")
        ) || []

        if (bookingsForDate.length > 0) {
            setSelectedBooking(bookingsForDate[0])
        }
    }

    const dateCellRender = (value: dayjs.Dayjs) => {
        const hasBooking = bookings?.some((b) => dayjs(b.beginningOfSession).isSame(value, "day"))
        return hasBooking ? <div><Badge/><CalendarOutlined/></div> : null
    }

    return (
        <>
            {isLoading && <Spin />}

            <Calendar
                onSelect={onSelectDate}
                fullCellRender={dateCellRender}
                fullscreen={false}
            />

            <BookingReviewModal
                booking={selectedBooking}
                open={!!selectedBooking}
                loading={updateStatusMutation.isPending}
                onClose={() => setSelectedBooking(null)}
                onDecision={(status) => {
                    if (selectedBooking) {
                        updateStatusMutation.mutate({
                            bookingId: selectedBooking.bookingId,
                            bookingStatus: status
                        })
                    }
                }}
            />
        </>
    )
}