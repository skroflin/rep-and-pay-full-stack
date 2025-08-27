import { useQuery } from "@tanstack/react-query";
import { useState } from "react";
import type { TrainerBookingResponse } from "../../utils/types/Booking";
import { getTrainerBookings } from "../../utils/api";
import dayjs from "dayjs";
import { Badge, Calendar, Spin } from "antd";
import { ScheduleOutlined } from "@ant-design/icons";
import BookingReviewModal from "./BookingReviewModal";

export default function NotificationsPage() {
    const [selectedBooking, setSelectedBooking] = useState<TrainerBookingResponse[]>([])
    const [drawerOpen, setDrawerOpen] = useState(false)

    const { data: bookings, isLoading } = useQuery<TrainerBookingResponse[]>({
        queryKey: ["trainer-bookings"],
        queryFn: getTrainerBookings
    })

    const onSelectDate = (date: dayjs.Dayjs) => {
        const bookingsForDate = bookings?.filter((b) =>
            dayjs(b.startOfSession).isSame(date, "day")
        ) || []

        if (bookingsForDate.length > 0) {
            setSelectedBooking(bookingsForDate)
            setDrawerOpen(true)
        } else {
            setSelectedBooking([])
            setDrawerOpen(false)
        }
    }

    const dateCellRender = (value: dayjs.Dayjs) => {
        const bookingsForDate = bookings?.filter((b) => dayjs(b.startOfSession).isSame(value, "day"))
        if (bookingsForDate?.length === 0) return null
        return (
            <div
                style={{
                    display: "flex",
                    justifyContent: "center",
                    gap: 2
                }}
            >
                {bookingsForDate?.map((b) => (
                    <Badge
                        key={b.bookingId}
                        count={<ScheduleOutlined style={{ color: b.bookingStatus === "pending" ? "#faad14" : b.bookingStatus === "approved" ? "#52c41a" : "#ff4d4f" }} />}
                    />
                    // <span
                    //     key={b.bookingId}
                    //     style={{
                    //         display: "inline-block",
                    //         width: 8,
                    //         height: 8,
                    //         borderRadius: "50%",
                    //         backgroundColor: b.bookingStatus === "pending" ? "#faad14" : b.bookingStatus === "approved" ? "#52c41a" : "#ff4d4f"
                    //     }}
                    //     title={`${b.userFirstName} ${b.userLastName} - ${dayjs(b.beginningOfSession).format("HH:mm")}`}
                    // />
                ))}
            </div>
        )
    }

    return (
        <>
            {isLoading && <Spin />}

            <Calendar
                onSelect={onSelectDate}
                cellRender={dateCellRender}
                fullscreen={false}
            />

            <BookingReviewModal
                bookings={selectedBooking}
                open={drawerOpen}
                onClose={() => setDrawerOpen(false)}
            />
        </>
    )
}