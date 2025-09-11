import { useQuery } from "@tanstack/react-query";
import { getMyBookings } from "../../../utils/api";
import { getRole, getUsername } from "../../../utils/helper";
import { useEffect, useState } from "react";
import { Button, Descriptions, Drawer, Space, Spin, Tag, Typography } from "antd";
import { ArrowLeftOutlined, ArrowRightOutlined } from "@ant-design/icons";
import dayjs from "dayjs";
import type { MyBookingResponse } from "../../../utils/types/user-authenticated/MyBooking";

export default function UserBookingDetails({
    open,
    onClose
}: {
    open: boolean
    onClose: () => void
}) {

    const role = getRole()

    const { data: bookings, isLoading: bookingsLoading } = useQuery<MyBookingResponse[]>({
        queryKey: ["user-bookings"],
        queryFn: getMyBookings,
        enabled: role === "user"
    })

    const [currentIndex, setCurrentIndex] = useState(0)

    useEffect(() => {
        if (open) setCurrentIndex(0)
    }, [open, bookings])

    if (!bookings || bookings.length === 0) return null

    const booking = bookings[currentIndex]

    const { Text, Title } = Typography

    const username = getUsername()
    console.log(username)

    return (
        <Drawer
            open={open}
            onClose={onClose}
            title={
                <span>
                    <Title level={5}>
                        Training sessions bookings for {username}
                    </Title>
                    <Text style={{ float: "right" }}>
                        {currentIndex + 1} of {bookings.length}
                    </Text>
                </span>
            }
            footer={
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
            }
            placement="right"
        >
            {bookingsLoading ? (
                <Spin />
            ) : (
                bookings && (
                    <Descriptions
                        column={1}
                        bordered
                        size="small"
                    >
                        <Descriptions.Item label="Trainer">
                            <Text strong>{booking.trainerFirstName} {booking.trainerLastName}</Text>
                        </Descriptions.Item>
                        <Descriptions.Item label="Start of session">
                            <Text strong>{dayjs(booking.startOfSession).format("HH:mm")}</Text>
                        </Descriptions.Item>
                        <Descriptions.Item label="End of session">
                            <Text strong>{dayjs(booking.endOfSession).format("HH:mm")}</Text>
                        </Descriptions.Item>
                        <Descriptions.Item label="Training type">
                            <Tag color="blue">{booking.trainingType.toUpperCase()}</Tag>
                        </Descriptions.Item>
                        <Descriptions.Item label="Training level">
                            <Tag color="geekblue">{booking.trainingLevel.toUpperCase()}</Tag>
                        </Descriptions.Item>
                        <Descriptions.Item label="Booking status">
                            <Text>
                                {booking.bookingStatus === "accepted" && <Tag color="green">{booking.bookingStatus.toUpperCase()}</Tag>}
                                {booking.bookingStatus === "rejected" && <Tag color="red">{booking.bookingStatus.toUpperCase()}</Tag>}
                                {booking.bookingStatus === "pending" && <Tag color="yellow">{booking.bookingStatus.toUpperCase()}</Tag>}
                            </Text>
                        </Descriptions.Item>
                    </Descriptions>
                )
            )}
        </Drawer>
    )
}