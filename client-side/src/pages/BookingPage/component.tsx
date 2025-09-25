import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import {
    Button,
    Calendar,
    Descriptions,
    Divider,
    List,
    Spin,
    Tooltip,
    Typography
} from "antd";
import { useState } from "react";
import { toast } from "react-toastify";
import {
    createMyBooking,
    getAvailableTrainingSessions,
    hasActiveMembership
} from "../../utils/api";
import dayjs, { Dayjs } from "dayjs";
import type { MyBookingRequest } from "../../utils/types/user-authenticated/MyBooking";
import type { TrainingSessionResponse } from "../../utils/types/TrainingSession";
import { formatDate } from "../../misc/formatDate";
import { FrownFilled, OrderedListOutlined, PlusCircleFilled } from "@ant-design/icons";
import { useNavigate } from "react-router";

export default function BookingPage() {
    const queryClient = useQueryClient()
    const [selectedDate, setSelectedDate] = useState<string>(dayjs().format("YYYY-MM-DD"))
    const [pendingSession, setPendingSession] = useState<string[]>([])

    const { data: sessions, isLoading } = useQuery<TrainingSessionResponse[]>({
        queryKey: ["available-sessions", selectedDate],
        queryFn: () => getAvailableTrainingSessions(selectedDate)
    })

    const { data: activeMembership } = useQuery<boolean>({
    queryKey: ["has-active-membership", selectedDate],
    queryFn: async () => {
        const res = await hasActiveMembership(selectedDate);
        if (res && typeof res === "object" && "active" in res) {
            return (res as { active: boolean }).active;
        }
        return res as boolean;
    }
});

    console.log("Active membership:", activeMembership)

    const bookingMutation = useMutation({
        mutationFn: (req: MyBookingRequest) => createMyBooking(req),
        onSuccess: () => {
            toast.success(`Booking request sent for ${formatDate(selectedDate)}!`)
            queryClient.invalidateQueries({ queryKey: ["available-sessions", selectedDate] })
        },
        onError: () => toast.error("Failed to create a booking!"),
    })

    const handleDataChange = (value: Dayjs) => {
        const dateStr = value.format("YYYY-MM-DD")
        setSelectedDate(dateStr)
    }

    const handleBooking = (session: TrainingSessionResponse) => {

        if (!activeMembership) {
            toast.warning("Please pay for your membership in order to book a training session!")
            return
        }

        setPendingSession(prev => [...prev, session.id])
        bookingMutation.mutate({
            trainingSessionId: session.id
        },
            {
                onError: () => {
                    setPendingSession(prev => prev.filter(id => id !== session.id))
                }
            })
    }

    const { Text, Title } = Typography
    const navigate = useNavigate()
    const isActiveMembership = activeMembership ?? false;

    return (
        <div
            style={{
                display: "block",
                maxWidth: 800,
                padding: 20,
                margin: "0 auto"
            }}
        >
            <Calendar
                fullscreen={false}
                onChange={handleDataChange}
            />
            <Title level={4}>
                <OrderedListOutlined /> Available sessions for {formatDate(selectedDate)}
                <Divider />
            </Title>

            {isLoading ? (
                <Spin />
            ) : (
                <List
                    bordered
                    dataSource={sessions || []}
                    renderItem={(session) => {
                        const isBooked = session.isAlreadyBooked;
                        const noMembership = !isActiveMembership;

                        let buttonText = "Book";
                        if (isBooked) buttonText = "Already booked";
                        else if (noMembership) buttonText = "Membership required";

                        return (
                            <List.Item>
                                <Descriptions>
                                    <Descriptions.Item label="Trainer">
                                        <Text strong>{session.trainerFirstName} {session.trainerLastName}</Text>
                                    </Descriptions.Item>
                                    <Descriptions.Item label="Type">
                                        <Text strong style={{ textTransform: "capitalize" }}>{session.trainingType}</Text>
                                    </Descriptions.Item>
                                    <Descriptions.Item label="Level">
                                        <Text strong style={{ textTransform: "capitalize" }}>{session.trainingLevel}</Text>
                                    </Descriptions.Item>
                                    <Descriptions.Item label="Start">
                                        <Text strong>{dayjs(session.beginningOfSession).format("HH:mm")}</Text>
                                    </Descriptions.Item>
                                    <Descriptions.Item label="End">
                                        <Text strong>{dayjs(session.endOfSession).format("HH:mm")}</Text>
                                    </Descriptions.Item>
                                    <Descriptions.Item>
                                        <Tooltip
                                            title={noMembership ? (
                                                <Text style={{ color: "white" }}>
                                                    You need an active membership in order to book a training session!
                                                    If you need one, please visit the following
                                                    <Button
                                                        type="link"
                                                        onClick={() => navigate("/membership")}
                                                        style={{ marginRight: "0em" }}
                                                    >
                                                        link!
                                                    </Button>
                                                </Text>
                                            ) : ""}
                                        >
                                            <Button
                                                type="primary"
                                                onClick={() => handleBooking(session)}
                                                loading={bookingMutation.isPending}
                                                disabled={
                                                    pendingSession.includes(session.id) ||
                                                    isBooked ||
                                                    noMembership
                                                }
                                                icon={<PlusCircleFilled />}
                                                size="small"
                                                style={{
                                                    fontSize: "14px"
                                                }}
                                            >
                                                {buttonText}
                                            </Button>
                                        </Tooltip>
                                    </Descriptions.Item>
                                </Descriptions>
                            </List.Item>
                        );
                    }}
                    pagination={{ pageSize: 1 }}
                    locale={{
                        emptyText: (
                            <Text strong>No sessions available for {formatDate(selectedDate)} <FrownFilled /></Text>
                        )
                    }}
                />
            )}
        </div>
    )
}