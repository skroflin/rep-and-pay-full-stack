import { Button, Drawer, Space, Descriptions } from "antd";
import type { TrainerBookingResponse } from "../../utils/types/Booking";
import dayjs from "dayjs";

interface BookingReviewModalProps {
    booking: TrainerBookingResponse | null
    open: boolean
    loading: boolean
    onClose: () => void
    onDecision: (bookingStatus: "approved" | "rejected") => void
}

export default function BookingReviewDrawer({
    booking,
    open,
    loading,
    onClose,
    onDecision
}: BookingReviewModalProps) {
    return (
        <Drawer
            open={open}
            onClose={onClose}
            title="Review booking request"
            placement="right"
            width={400}
            footer={
                <Space style={{ float: "right" }}>
                    <Button
                        danger
                        onClick={() => onDecision("rejected")}
                        loading={loading}
                    >
                        Reject
                    </Button>
                    <Button
                        type="primary"
                        onClick={() => onDecision("approved")}
                        loading={loading}
                    >
                        Approve
                    </Button>
                </Space>
            }
        >
            {booking && (
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
                        <p>{dayjs(booking.beginningOfSession).format("DD.MM.YYYY HH:mm")}</p>
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