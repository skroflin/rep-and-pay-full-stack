import { useQuery } from "@tanstack/react-query";
import { Col, Row, Statistic, theme } from "antd";
import { Content } from "antd/es/layout/layout";
import { getNumOfAcceptedTrainerBookings, getNumOfBeginnerTrainingSessions, getNumOfIntermediateTrainingSessions, getNumOfMyTrainingSessions, getNumOfPendingTrainerBookings, getNumOfTrainerBookings } from "../../utils/api";
import { getRole } from "../../utils/helper";

export default function HomePage() {
    // const {
    //     token: { colorBgContainer, borderRadiusLG },
    // } = theme.useToken()

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

    return (
        <>
            <Row gutter={16}>
                {role === "coach" && (
                    <>
                        <Col span={14}>
                            <Statistic title="Number of training sessions" value={numOfMyTrainingSessions} />
                        </Col>
                        <Col span={14}>
                            <Statistic title="Number of training bookings" value={numOfTrainerBookings} />
                        </Col>
                        <Col span={14}>
                            <Statistic title="Number of bookings" value={numOfPendingTrainerBookings} suffix={`/${numOfTrainerBookings}`} />
                        </Col>
                        <Col span={14}>
                            <Statistic title="Number of bookings" value={numOfAcceptedTrainerBookings} suffix={` /${numOfTrainerBookings}`} />
                        </Col>
                        <Col span={14}>
                            <Statistic title="Number of beginner training sessions" value={numOfBeginnerTrainingSessions} suffix={` /${numOfMyTrainingSessions}`}/>
                        </Col>
                        <Col span={14}>
                            <Statistic title="Number of intermediate training sessions" value={numOfIntermediateTrainingSessions} suffix={` /${numOfMyTrainingSessions}`}/>
                        </Col>
                    </>
                )}
                {role === "user" && (
                    <>
                        <Content>
                            User content
                        </Content>
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