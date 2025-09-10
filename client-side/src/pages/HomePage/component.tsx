import { useQueries, useQuery } from "@tanstack/react-query";
import { Button, Card, Descriptions, Divider, Flex, List, Spin, Statistic, Tag, Typography } from "antd";
import { Content } from "antd/es/layout/layout";
import {
    getMyBookings,
    getNumOfAcceptedTrainerBookings,
    getNumOfActiveMemberships,
    getNumOfAdvancedTrainingSessions,
    getNumOfBeginnerTrainingSessions,
    getNumOfExpiredMemberships,
    getNumOfIntermediateTrainingSessions,
    getNumOfMemberships,
    getNumOfMyBookings,
    getNumOfMyTrainingSessions,
    getNumOfMyUserTrainingSessions,
    getNumOfPendingTrainerBookings,
    getNumOfTrainerBookings
} from "../../utils/api";
import { getRole } from "../../utils/helper";
import CountUp from "react-countup";
import type { MyBookingResponse } from "../../utils/types/user-authenticated/MyBooking";
import dayjs from "dayjs";
import { ClockCircleFilled, ClockCircleOutlined } from "@ant-design/icons";
import { useState } from "react";
import ActiveMembershipDetails from "./ActiveMembershipDetails";
import ExpiredMembershipDetails from "./ExpiredMembershipDetails";

export default function HomePage() {

    const role = getRole()

    const [activeDrawerOpen, setActiveDrawerOpen] = useState(false)
    const [expiredDrawerOpen, setExpiredDrawerOpen] = useState(false)

    const results = useQueries({
        queries: [
            {
                queryKey: ["num-of-my-training-sessions"],
                queryFn: getNumOfMyTrainingSessions,
                enabled: role === "coach"
            },
            {
                queryKey: ["num-of-trainer-bookings"],
                queryFn: getNumOfTrainerBookings,
                enabled: role === "coach"
            },
            {
                queryKey: ["num-of-pending-trainer-bookings"],
                queryFn: getNumOfPendingTrainerBookings,
                enabled: role === "coach"
            },
            {
                queryKey: ["num-of-accepted-trainer-bookings"],
                queryFn: getNumOfAcceptedTrainerBookings,
                enabled: role === "coach"
            },
            {
                queryKey: ["num-of-beginner-training-sessions"],
                queryFn: getNumOfBeginnerTrainingSessions,
                enabled: role === "coach"
            },
            {
                queryKey: ["num-of-intermediate-training-sessions"],
                queryFn: getNumOfIntermediateTrainingSessions,
                enabled: role === "coach"
            },
            {
                queryKey: ["num-of-user-training-sessions"],
                queryFn: getNumOfMyUserTrainingSessions,
                enabled: role === "user"
            },
            {
                queryKey: ["user-bookings"],
                queryFn: getMyBookings,
                enabled: role === "user"
            },
            {
                queryKey: ["num-of-advanced-training-sessions"],
                queryFn: getNumOfAdvancedTrainingSessions,
                enabled: role === "coach"
            },
            {
                queryKey: ["num-of-memberships"],
                queryFn: getNumOfMemberships,
                enabled: role === "superuser"
            },
            {
                queryKey: ["num-of-active-memberships"],
                queryFn: getNumOfActiveMemberships,
                enabled: role === "superuser"
            },
            {
                queryKey: ["num-of-expired-memberships"],
                queryFn: getNumOfExpiredMemberships,
                enabled: role === "superuser"
            }
        ]
    });

    const { data: bookings, isLoading } = useQuery<MyBookingResponse[]>({
        queryKey: ["my-booking-request"],
        queryFn: () => getMyBookings(),
        enabled: role === "user"
    })

    const [
        numOfMyTrainingSessions,
        numOfTrainerBookings,
        numOfPendingTrainerBookings,
        numOfAcceptedTrainerBookings,
        numOfBeginnerTrainingSessions,
        numOfIntermediateTrainingSessions,
        numOfUserBookings,
        numOfUserTrainingSessions,
        numOfAdvancedTrainingSessions,
        numOfMemberships,
        numOfActiveMemberships,
        numOfExpiredMemberships
    ] = results.map((r: { data: any; }) => r.data);

    const { Text, Title } = Typography

    return (
        <>
            <Flex vertical justify="space-evenly" align="start" style={{ marginTop: 30, minHeight: "80vh" }} wrap>
                {role === "coach" && (
                    <>
                        <Flex gap={24}>
                            <Card variant="borderless" style={{ width: 350 }}>
                                <Statistic
                                    title="Number of training bookings"
                                    value={numOfTrainerBookings}
                                    formatter={value => <CountUp end={Number(value)} duration={2} />}
                                />
                            </Card>
                            <Card variant="borderless" style={{ width: 350 }}>
                                <Statistic
                                    title="Number of pending booking requests"
                                    value={numOfPendingTrainerBookings}
                                    suffix={`/${numOfTrainerBookings}`}
                                    formatter={value => <CountUp end={Number(value)} duration={2} />}
                                />
                            </Card>
                        </Flex>
                        <Divider />
                        <Flex gap={24}>
                            <Card variant="borderless" style={{ width: 350 }}>
                                <Statistic
                                    title="Number of accepted booking requests"
                                    value={numOfAcceptedTrainerBookings}
                                    suffix={` /${numOfTrainerBookings}`}
                                    formatter={value => <CountUp end={Number(value)} duration={2} />}
                                />
                            </Card>
                            <Card variant="borderless" style={{ width: 350 }}>
                                <Statistic
                                    title="Number of training sessions"
                                    value={numOfMyTrainingSessions}
                                    formatter={value => <CountUp end={Number(value)} duration={2} />}
                                />
                            </Card>
                        </Flex>
                        <Divider />
                        <Flex gap={24}>
                            <Card variant="borderless" style={{ width: 350 }}>
                                <Statistic
                                    title="Number of beginner training sessions"
                                    value={numOfBeginnerTrainingSessions}
                                    suffix={` /${numOfMyTrainingSessions}`}
                                    formatter={value => <CountUp end={Number(value)} duration={2} />}
                                />
                            </Card>
                            <Card variant="borderless" style={{ width: 350 }}>
                                <Statistic
                                    title="Number of intermediate training sessions"
                                    value={numOfIntermediateTrainingSessions}
                                    suffix={` /${numOfMyTrainingSessions}`}
                                    formatter={value => <CountUp end={Number(value)} duration={2} />}
                                />
                            </Card>
                        </Flex>
                        {/* <Card variant="borderless" style={{ width: 350 }}>
                            <Statistic title="Number of advanced training sessions" value={numOfAdvancedTrainingSessions} suffix={` /${numOfMyTrainingSessions}`} />
                        </Card> */}
                    </>
                )}
                {role === "user" && (
                    <>
                        <Flex gap={16}>
                            <Card variant="borderless" style={{ width: 350 }}>
                                <Statistic
                                    title="Number of my bookings"
                                    value={numOfUserBookings ?? 0}
                                    formatter={value => <CountUp end={Number(value)} duration={2} />}
                                />
                            </Card>
                            <Card variant="borderless" style={{ width: 350 }}>
                                <Statistic
                                    title="Number of my training sessions"
                                    value={numOfUserTrainingSessions}
                                    formatter={value => <CountUp end={Number(value)} duration={2} />}
                                />
                            </Card>
                        </Flex>
                        <Divider />
                        <Flex>
                            <Card variant="borderless" style={{ width: 350 }} title="My bookings">
                                <Content>
                                    {isLoading ? (
                                        <Spin />
                                    ) : bookings && bookings.length > 0 ? (
                                        <List
                                            pagination={{ pageSize: 3 }}
                                            bordered
                                            dataSource={bookings}
                                            renderItem={(booking) => (
                                                <List.Item>
                                                    <Descriptions column={1} size="small" style={{ width: "100%" }}>
                                                        <Descriptions.Item>
                                                            <Text strong>{booking.trainingType.toUpperCase()} - {booking.trainingLevel.toUpperCase()}</Text>
                                                        </Descriptions.Item>
                                                        <Descriptions.Item>
                                                            <Text>{booking.trainerFirstName} {booking.trainerLastName}</Text>
                                                        </Descriptions.Item>
                                                        <Descriptions.Item label={<ClockCircleFilled style={{ fontSize: "12px" }} />}>
                                                            <Text>From {dayjs(booking.startOfSession).format("HH:mm")}</Text>
                                                        </Descriptions.Item>
                                                        <Descriptions.Item label={<ClockCircleOutlined style={{ fontSize: "12px" }} />}>
                                                            <Text>To {dayjs(booking.endOfSession).format("HH:mm")}</Text>
                                                        </Descriptions.Item>
                                                        <Descriptions.Item>
                                                            <Text>
                                                                {booking.bookingStatus === "accepted" && <Tag color="green">{booking.bookingStatus.toUpperCase()}</Tag>}
                                                                {booking.bookingStatus === "rejected" && <Tag color="red">{booking.bookingStatus.toUpperCase()}</Tag>}
                                                                {booking.bookingStatus === "pending" && <Tag color="yellow">{booking.bookingStatus.toUpperCase()}</Tag>}
                                                            </Text>
                                                        </Descriptions.Item>
                                                    </Descriptions>
                                                </List.Item>
                                            )}
                                        >
                                        </List>
                                    ) : (
                                        <Text></Text>
                                    )}
                                </Content>
                            </Card>
                        </Flex>
                    </>
                )}
                {role === "superuser" && (
                    <>
                        <Flex gap={16}>
                            <Card variant="borderless" style={{ width: 350 }}>
                                <Statistic
                                    title="Number of memberships"
                                    value={numOfMemberships ?? 0}
                                    formatter={value => <CountUp end={Number(value)} duration={2} />}
                                />
                            </Card>
                            <Card variant="borderless" style={{ width: 350 }} extra={<Button type="link" onClick={() => setActiveDrawerOpen(true)}>View details</Button>}>
                                <Statistic
                                    title="Number of active memberships"
                                    value={numOfActiveMemberships ?? 0}
                                    suffix={` /${numOfMemberships}`}
                                    formatter={value => <CountUp end={Number(value)} duration={2} />}
                                />
                            </Card>
                            <Card variant="borderless" style={{ width: 350 }} extra={<Button type="link" onClick={() => setExpiredDrawerOpen(true)}>View details</Button>}>
                                <Statistic
                                    title="Number of expired memberships"
                                    value={numOfExpiredMemberships ?? 0}
                                    suffix={` /${numOfMemberships}`}
                                    formatter={value => <CountUp end={Number(value)} duration={2} />}
                                />
                            </Card>
                        </Flex>
                    </>
                )}
            </Flex>

            <ActiveMembershipDetails
                open={activeDrawerOpen}
                onClose={() => setActiveDrawerOpen(false)}
            />

            <ExpiredMembershipDetails
                open={expiredDrawerOpen}
                onClose={() => setExpiredDrawerOpen(false)} 
            />
        </>
    )

}