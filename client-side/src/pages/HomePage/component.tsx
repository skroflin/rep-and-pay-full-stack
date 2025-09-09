import { useQueries } from "@tanstack/react-query";
import { Card, Flex, Statistic } from "antd";
import { Content } from "antd/es/layout/layout";
import { getMyBookings, getNumOfAcceptedTrainerBookings, getNumOfAdvancedTrainingSessions, getNumOfBeginnerTrainingSessions, getNumOfIntermediateTrainingSessions, getNumOfMyBookings, getNumOfMyTrainingSessions, getNumOfMyUserTrainingSessions, getNumOfPendingTrainerBookings, getNumOfTrainerBookings } from "../../utils/api";
import { getRole } from "../../utils/helper";
import type { BookingResponse } from "../../utils/types/Booking";
import CountUp from "react-countup";

export interface HomePageProps {
    userBookings: BookingResponse[]
}

export default function HomePage() {

    const role = getRole()

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
                queryKey: ["num-of-user-bookings"],
                queryFn: getNumOfMyBookings,
                enabled: role === "user"
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
            }
        ]
    });

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
        userBookings //napraviti logiku mapiranja i prikaz podataka za rezervacije
    ] = results.map((r: { data: any; }) => r.data);

    return (
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
                    <Flex>
                        <Card variant="borderless" style={{ width: 350 }} title="My bookings">
                            <Content>
                                {/* logika mapiranja ovdje! */}
                            </Content>
                        </Card>
                    </Flex>
                </>
            )}
            {role === "superuser" && (
                <Card variant="borderless" style={{ width: 350 }}>
                    <Content>
                        Superuser content
                    </Content>
                </Card>
            )}
        </Flex>
    )
}