import { useQuery } from "@tanstack/react-query";
import { Card, Col, Row, Statistic } from "antd";
import { Content } from "antd/es/layout/layout";
import { getNumOfAcceptedTrainerBookings, getNumOfBeginnerTrainingSessions, getNumOfIntermediateTrainingSessions, getNumOfMyBookings, getNumOfMyTrainingSessions, getNumOfMyUserTrainingSessions, getNumOfPendingTrainerBookings, getNumOfTrainerBookings } from "../../utils/api";
import { getRole } from "../../utils/helper";

export default function HomePage() {

    const role = getRole()

    const { data: numOfMyTrainingSessions } = useQuery({
        queryKey: ["num-of-my-training-sessions"],
        queryFn: () => getNumOfMyTrainingSessions(),
        enabled: role === "coach"
    })

    const { data: numOfTrainerBookings } = useQuery({
        queryKey: ["num-of-trainer-bookings"],
        queryFn: () => getNumOfTrainerBookings(),
        enabled: role === "coach"
    })

    const { data: numOfPendingTrainerBookings } = useQuery({
        queryKey: ["num-of-pending-trainer-bookings"],
        queryFn: () => getNumOfPendingTrainerBookings(),
        enabled: role === "coach"
    })

    const { data: numOfAcceptedTrainerBookings } = useQuery({
        queryKey: ["num-of-accepted-trainer-bookings"],
        queryFn: () => getNumOfAcceptedTrainerBookings(),
        enabled: role === "coach"
    })

    const { data: numOfBeginnerTrainingSessions } = useQuery({
        queryKey: ["num-of-beginner-training-sessions"],
        queryFn: () => getNumOfBeginnerTrainingSessions(),
        enabled: role === "coach"
    })

    const { data: numOfIntermediateTrainingSessions } = useQuery({
        queryKey: ["num-of-intermediate-training-sessions"],
        queryFn: () => getNumOfIntermediateTrainingSessions(),
        enabled: role === "coach"
    })

    const { data: numOfUserBookings } = useQuery({
        queryKey: ["num-of-user-bookings"],
        queryFn: () => getNumOfMyBookings(),
        enabled: role === "user"
    })

    const { data: numOfUserTrainingSessions } = useQuery({
        queryKey: ["num-of-user-training-sessions"],
        queryFn: () => getNumOfMyUserTrainingSessions(),
        enabled: role === "user"
    })

    return (
        <>
            <Row style={{
                marginTop: 30
            }}>
                {role === "coach" && (
                    <>
                        <Col span={13} offset={10}>
                            <Card variant="borderless">
                                <Statistic title="Number of training sessions" value={numOfMyTrainingSessions} />
                            </Card>
                        </Col>
                        <Col span={13} offset={10}>
                            <Card variant="borderless">
                                <Statistic title="Number of training bookings" value={numOfTrainerBookings} />
                            </Card>
                        </Col>
                        <Col span={13} offset={10}>
                            <Card variant="borderless">
                                <Statistic title="Number of bookings" value={numOfPendingTrainerBookings} suffix={`/${numOfTrainerBookings}`} />
                            </Card>
                        </Col>
                        <Col span={13} offset={10}>
                            <Card variant="borderless">
                                <Statistic title="Number of bookings" value={numOfAcceptedTrainerBookings} suffix={` /${numOfTrainerBookings}`} />
                            </Card>
                        </Col>
                        <Col span={13} offset={10}>
                            <Card variant="borderless">
                                <Statistic title="Number of beginner training sessions" value={numOfBeginnerTrainingSessions} suffix={` /${numOfMyTrainingSessions}`} />
                            </Card>
                        </Col>
                        <Col span={13} offset={10}>
                            <Card variant="borderless">
                                <Statistic title="Number of intermediate training sessions" value={numOfIntermediateTrainingSessions} suffix={` /${numOfMyTrainingSessions}`} />
                            </Card>
                        </Col>
                    </>
                )}
                {role === "user" && (
                    <>
                        <Col span={13}>
                            <Card variant="borderless">
                                <Statistic title="Number of my bookings" value={numOfUserBookings} />
                            </Card>
                        </Col>
                        <Col span={13}>
                            <Card variant="borderless">
                                <Statistic title="Number of my training sessions" value={numOfUserTrainingSessions} />
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