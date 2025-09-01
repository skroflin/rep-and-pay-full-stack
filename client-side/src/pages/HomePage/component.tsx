import { useQueries } from "@tanstack/react-query";
import { Card, Col, Flex, Row, Statistic, Tooltip } from "antd";
import { Content } from "antd/es/layout/layout";
import { getMyBookings, getNumOfAcceptedTrainerBookings, getNumOfAdvancedTainingSessions, getNumOfBeginnerTrainingSessions, getNumOfIntermediateTrainingSessions, getNumOfMyBookings, getNumOfMyTrainingSessions, getNumOfMyUserTrainingSessions, getNumOfPendingTrainerBookings, getNumOfTrainerBookings } from "../../utils/api";
import { getRole } from "../../utils/helper";
import type { BookingResponse } from "../../utils/types/Booking";

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
                queryKey: ["num-of-advanced-sessions"],
                queryFn: getNumOfAdvancedTainingSessions,
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
        <>
            <Row style={{
                marginTop: 30
            }}>
                {role === "coach" && (
                    <>
                        <Col span={8} offset={6}>
                            <Card variant="borderless">
                                <Statistic title="Number of training sessions" value={numOfMyTrainingSessions} />
                            </Card>
                        </Col>
                        <Col span={8} offset={6}>
                            <Card variant="borderless">
                                <Statistic title="Number of training bookings" value={numOfTrainerBookings} />
                            </Card>
                        </Col>
                        <Col span={8} offset={6}>
                            <Card variant="borderless">
                                <Statistic title="Number of pending booking requests" value={numOfPendingTrainerBookings} suffix={`/${numOfTrainerBookings}`} />
                            </Card>
                        </Col>
                        <Col span={8} offset={6}>
                            <Card variant="borderless">
                                <Statistic title="Number of accepted booking requests" value={numOfAcceptedTrainerBookings} suffix={` /${numOfTrainerBookings}`} />
                            </Card>
                        </Col>
                        <Col span={8} offset={6}>
                            <Card variant="borderless">
                                <Statistic title="Number of beginner training sessions" value={numOfBeginnerTrainingSessions} suffix={` /${numOfMyTrainingSessions}`} />
                            </Card>
                        </Col>
                        <Col span={8} offset={6}>
                            <Card variant="borderless">
                                <Statistic title="Number of intermediate training sessions" value={numOfIntermediateTrainingSessions} suffix={` /${numOfMyTrainingSessions}`} />
                            </Card>
                        </Col>
                        <Col span={8} offset={6}>
                            <Card variant="borderless">
                                <Statistic title="Number of advanced training sessions" value={numOfAdvancedTrainingSessions} suffix={` /${numOfMyTrainingSessions}`} />
                            </Card>
                        </Col>
                    </>
                )}
                {role === "user" && (
                    <>
                        <Col span={13} offset={10}>
                            <Card variant="borderless">
                                <Statistic title="Number of my bookings" value={numOfUserBookings} />
                            </Card>
                        </Col>
                        <Col span={13} offset={10}>
                            <Card variant="borderless">
                                <Statistic title="Number of my training sessions" value={numOfUserTrainingSessions} />
                            </Card>
                        </Col>
                        <Col span={13} offset={10}>
                            <Card variant="borderless" title="My bookings">
                                <Content>
                                    {/* logika mapiranja ovdje! */}
                                </Content>
                            </Card>
                        </Col>
                    </>
                )}
                {role === "superuser" && (
                    <>
                        <Content>
                            Superuser content
                        </Content>
                    </>
                )}
            </Row>
        </>
    )
}