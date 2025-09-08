import { useQuery } from "@tanstack/react-query";
import { useState } from "react";
import type { TrainerBookingResponse } from "../../utils/types/Booking";
import { getTrainerBookings } from "../../utils/api";
import dayjs from "dayjs";
import { Badge, Calendar, Spin } from "antd";
import { BookOutlined, PlusOutlined, ScheduleOutlined } from "@ant-design/icons";
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
        const bookingsForDate = bookings?.filter((b) => dayjs(b.startOfSession).isSame(value, "day")) || []
        if (bookingsForDate.length === 0) return null

        if (bookingsForDate.length > 1) {
            const firstStatus = bookingsForDate[0].bookingStatus
            return (
                <div
                    style={{
                        display: "flex",
                        justifyContent: "center",
                        gap: 2
                    }}
                >
                    <Badge
                        count={
                            <span>
                                <ScheduleOutlined color={
                                    firstStatus === "pending" ? "orange" :
                                        firstStatus === "accepted" ? "green" :
                                            firstStatus === "rejected" ? "red" : "gray"}
                                />
                                <PlusOutlined
                                    style={{
                                        marginLeft: 4
                                    }}
                                    color={
                                        firstStatus === "pending" ? "orange" :
                                            firstStatus === "accepted" ? "green" :
                                                firstStatus === "rejected" ? "red" : "gray"}
                                />
                            </span>
                        }
                    />
                </div>
            )
        }
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
                        count={
                            <ScheduleOutlined style={{
                                color: b.bookingStatus === "pending" ? "orange" : b.bookingStatus === "accepted" ? "green" : b.bookingStatus === "rejected" ? "red" : "gray"
                            }}
                            />}
                    />
                ))}
            </div>
        )
    }

    return (
        <div
            style={{
                maxWidth: 900,
                margin: "0 auto",
                padding: 20
            }}>
            <h1>
                Booking requests <BookOutlined />
            </h1>
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
        </div>
    )
}