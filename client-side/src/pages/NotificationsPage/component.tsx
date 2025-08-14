import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import type { TrainerBookingResponse } from "../../utils/types/Booking";
import { getTrainerBookings, updateBookingStatus } from "../../utils/api";
import dayjs from "dayjs";
import { Badge, Button, Calendar, Drawer, List, Space, Spin, Typography } from "antd";
import { CalendarOutlined } from "@ant-design/icons";

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
        dayjs(b.beginningOfSession).isSame(selectedDate, "day"),
    ) || []

    const dateCellRender = (value: dayjs.Dayjs) => {
        const hasPending = bookings?.some(
            (b) => dayjs(b.beginningOfSession).isSame(value, "day")
        )
        if (hasPending) {
            return (
                <div
                    style={{ textAlign: "center" }}
                >
                    <Badge
                        status="processing"
                        style={{
                            marginRight: 4
                        }}
                    />
                    <CalendarOutlined
                        style={{
                            color: "#1890ff",
                            fontSize: "16px"
                        }}
                    />
                </div>
            )
        }
        return null
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
                                        {dayjs(booking.beginningOfSession).format("HH:mm")} - {dayjs(booking.endOfSession).format("HH:mm")}
                                    </Text>
                                    <Text>Status: {booking.bookingStatus}</Text>
                                </Space>
                            </List.Item>
                        )}
                    />
                </>
            )}

            <Drawer
                placement="right"
                width={400}
                title="Review Booking Request"
                open={!!selectedBooking}
                onClose={() => setSelectedBooking(null)}
                extra={[
                    <Space>
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
                    </Space>
                ]}
            >
                {selectedBooking && (
                    <div>
                        <div>
                            <Text strong>User:</Text>
                            <Text>{selectedBooking.userFirstName} {selectedBooking.userLastName}</Text>
                        </div>
                        <div style={{ marginTop: 8 }}>
                            <Text strong>Training type:</Text>
                            <Text>{selectedBooking.trainingType}</Text>
                        </div>
                        <div style={{ marginTop: 8 }}>
                            <Text strong>Session:</Text>
                            <Text>From: {dayjs(selectedBooking.beginningOfSession).format("HH:mm")} to: {dayjs(selectedBooking.endOfSession).format("HH:mm")}</Text>
                        </div>
                        <div style={{ marginTop: 8 }}>
                            <Text strong>Booking status</Text>
                            <Text>{selectedBooking.bookingStatus}</Text>
                        </div>
                    </div>
                )}
            </Drawer>
        </>
    )
}