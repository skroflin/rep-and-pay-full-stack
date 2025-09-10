import { Button, Drawer, Space, Descriptions, Typography } from "antd";
import type { TrainerBookingResponse } from "../../utils/types/Booking";
import dayjs from "dayjs";
import { useEffect, useState } from "react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { updateBookingStatus } from "../../utils/api";
import {
    ArrowLeftOutlined,
    ArrowRightOutlined,
    CheckCircleOutlined,
    CloseCircleOutlined,
    SnippetsOutlined
} from "@ant-design/icons";
import { formatDate } from "../../misc/formatDate";

export default function BookingReviewDrawer({
    bookings,
    open,
    onClose
}: {
    bookings: TrainerBookingResponse[]
    open: boolean
    onClose: () => void
}) {

    const queryClient = useQueryClient()

    const updateStatusMutation = useMutation({
        mutationFn: ({ bookingId, bookingStatus }: { bookingId: string, bookingStatus: "accepted" | "rejected" }) =>
            updateBookingStatus(bookingId, { bookingStatus }),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["trainer-bookings"] })
            onClose()
        }
    })

    const [currentIndex, setCurrentIndex] = useState(0)

    useEffect(() => {
        if (open) setCurrentIndex(0)
    }, [open, bookings])

    if (!bookings || bookings.length === 0) return null

    const booking = bookings[currentIndex]

    const { Text } = Typography

    return (
        <Drawer
            open={open}
            onClose={onClose}
            title={
                <span>
                    <Text style={{
                        float: "left"
                    }}>
                        Review booking for {formatDate(dayjs(booking.startOfSession).format("YYYY-MM-DD"))}
                    </Text>
                    <Text style={{
                        float: "right"
                    }}>
                        {currentIndex + 1} of {bookings.length}<SnippetsOutlined style={{ marginLeft: 4 }} />
                    </Text>
                </span>
            }
            placement="right"
            width={650}
            footer={
                <div>
                    <Space style={{
                        float: "left"
                    }}>
                        <Button
                            danger
                            loading={updateStatusMutation.isPending}
                            onClick={() =>
                                updateStatusMutation.mutate({
                                    bookingId: booking.bookingId,
                                    bookingStatus: "rejected"
                                })
                            }
                            icon={<CloseCircleOutlined />}
                        >
                            Reject
                        </Button>
                        <Button
                            type="primary"
                            loading={updateStatusMutation.isPending}
                            onClick={() =>
                                updateStatusMutation.mutate({
                                    bookingId: booking.bookingId,
                                    bookingStatus: "accepted"
                                })
                            }
                            icon={<CheckCircleOutlined />}
                        >
                            Approve
                        </Button>
                    </Space>
                    <Space style={{ float: "right" }}>
                        <Button
                            disabled={currentIndex === 0}
                            onClick={() => setCurrentIndex((i) => i - 1)}
                            icon={
                                <ArrowLeftOutlined />
                            }
                        >
                            Back
                        </Button>
                        <Button
                            disabled={currentIndex === bookings.length - 1}
                            onClick={() => setCurrentIndex((i) => i + 1)}
                            icon={
                                <ArrowRightOutlined />
                            }
                        >
                            Next
                        </Button>
                    </Space>
                </div>
            }
        >
            {bookings && (
                <Descriptions column={1} bordered size="small">
                    <Descriptions.Item label="User">
                        <Text>{booking.userFirstName} {booking.userLastName}</Text>
                    </Descriptions.Item>
                    <Descriptions.Item label="Training type">
                        <Text style={{ textTransform: "capitalize" }}>{booking.trainingType}</Text>
                    </Descriptions.Item>
                    <Descriptions.Item label="Training level">
                        <Text style={{ textTransform: "capitalize" }}>{booking.trainingLevel}</Text>
                    </Descriptions.Item>
                    <Descriptions.Item label="Session start">
                        <Text>{dayjs(booking.startOfSession).format("HH:mm")}</Text>
                    </Descriptions.Item>
                    <Descriptions.Item label="Session end">
                        <Text>{dayjs(booking.endOfSession).format("HH:mm")}</Text>
                    </Descriptions.Item>
                    <Descriptions.Item label="Booking status">
                        <Text style={{ textTransform: "capitalize" }}>
                            {booking.bookingStatus === "pending" ? (
                                <Text type="warning">Pending</Text>
                            ) : booking.bookingStatus === "accepted" ? (
                                <Text type="success">Approved</Text>
                            ) : booking.bookingStatus === "rejected" ? (
                                <Text type="danger">Rejected</Text>
                            ) : null}
                        </Text>
                    </Descriptions.Item>
                </Descriptions>
            )}
        </Drawer>
    )
}