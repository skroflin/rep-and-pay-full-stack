import { Button, Form, Input, Modal, Select, Space } from "antd";
import type { TrainerBookingResponse } from "../../utils/types/Booking";
import dayjs from "dayjs";

interface BookingReveiwModalProps {
    booking: TrainerBookingResponse | null
    open: boolean
    loading: boolean
    onClose: () => void
    onDecision: (bookingStatus: "APPROVED" | "REJECTED") => void
}

export default function BookingReviewModal({
    booking,
    open,
    loading,
    onClose,
    onDecision
}: BookingReveiwModalProps) {
    const [form] = Form.useForm()
    return (
        <Modal
            open={open}
            onCancel={onClose}
            title="Review booking request"
            footer={
                <Space>
                    <Button
                        danger
                        onClick={() => onDecision("REJECTED")}
                        loading={loading}
                    >
                        Reject
                    </Button>
                    <Button
                        type="primary"
                        onClick={() => onDecision("APPROVED")}
                        loading={loading}
                    >
                        Approve
                    </Button>
                </Space>
            }
        >
            {booking && (
                <Form
                    layout="vertical"
                    form={form}
                    initialValues={{
                        userName: `${booking.userFirstName} ${booking.userLastName}`,
                        trainingType: `${booking.trainingType}`,
                        sessionTime: `From: ${dayjs(booking.beginningOfSession)} to: ${dayjs(booking.endOfSession)}`,
                        bookingStatus: booking.bookingStatus
                    }}
                >
                    <Form.Item
                        label="User"
                    >
                        <Input readOnly value={`${booking.userFirstName} ${booking.userLastName}`} />
                    </Form.Item>
                    <Form.Item
                        label="Training type"
                    >
                        <Input style={{ textTransform: "capitalize" }} readOnly value={`${booking.trainingType}`} />
                    </Form.Item>
                    <Form.Item
                        label="Session start"
                    >
                        <Input readOnly value={`${dayjs(booking.beginningOfSession)}`} />
                    </Form.Item>
                    <Form.Item
                        label="Session end"
                    >
                        <Input readOnly value={`${dayjs(booking.endOfSession)}`} />
                    </Form.Item>
                    <Form.Item
                        label="Booking status"
                    >
                        <Select style={{ textTransform: "capitalize" }} value={booking.bookingStatus} disabled>
                            <Select.Option value="PENDING">Pending</Select.Option>
                            <Select.Option value="APPROVED">Approved</Select.Option>
                            <Select.Option value="REJECTED">Rejected</Select.Option>
                        </Select>
                    </Form.Item>
                </Form>
            )}
        </Modal>
    )
}