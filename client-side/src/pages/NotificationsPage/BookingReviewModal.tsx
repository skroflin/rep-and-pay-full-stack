import { Button, Drawer, Space, Descriptions } from "antd";
import type { TrainerBookingResponse } from "../../utils/types/Booking";
import dayjs from "dayjs";
import { useEffect, useState } from "react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { updateBookingStatus } from "../../utils/api";
import { ArrowLeftOutlined, ArrowRightOutlined, CheckCircleOutlined, CloseCircleOutlined } from "@ant-design/icons";

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
        mutationFn: ({ bookingId, bookingStatus }: { bookingId: string, bookingStatus: "approved" | "rejected" }) =>
            updateBookingStatus(bookingId, { bookingStatus }),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["trainer-bookings"] })
            onClose()
        }
    })

    const [currentIndex, setCurrentIndex] = useState(0);

    useEffect(() => {
        if (open) setCurrentIndex(0)
    }, [open, bookings])

    if (!bookings || bookings.length === 0) return null

    const booking = bookings[currentIndex]

    return (
        <Drawer
            open={open}
            onClose={onClose}
            title="Review booking request"
            placement="right"
            width={500}
            footer={
                <div>
                    <Space style={{
                        float: "left"
                    }}>
                        <Button
                            danger
                            onClick={() => updateStatusMutation.mutate({ bookingId: booking.bookingId, bookingStatus: "rejected" })}
                            icon={<CloseCircleOutlined />}
                        >
                            Reject
                        </Button>
                        <Button
                            type="primary"
                            onClick={() => updateStatusMutation.mutate({ bookingId: booking.bookingId, bookingStatus: "approved" })}
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
                        <p>{booking.userFirstName} {booking.userLastName}</p>
                    </Descriptions.Item>
                    <Descriptions.Item label="Training type">
                        <p style={{ textTransform: "capitalize" }}>{booking.trainingType}</p>
                    </Descriptions.Item>
                    <Descriptions.Item label="Training level">
                        <p style={{ textTransform: "capitalize" }}>{booking.trainingLevel}</p>
                    </Descriptions.Item>
                    <Descriptions.Item label="Session start">
                        <p>{dayjs(booking.startOfSession).format("DD.MM.YYYY HH:mm")}</p>
                    </Descriptions.Item>
                    <Descriptions.Item label="Session end">
                        <p>{dayjs(booking.endOfSession).format("DD.MM.YYYY HH:mm")}</p>
                    </Descriptions.Item>
                    <Descriptions.Item label="Booking status">
                        <p style={{ textTransform: "capitalize" }}>{booking.bookingStatus}</p>
                    </Descriptions.Item>
                </Descriptions>
            )}
        </Drawer>
    )
}