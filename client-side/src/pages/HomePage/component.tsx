import { useQuery } from "@tanstack/react-query";
import { Col, Row, Statistic, theme } from "antd";
import { Content } from "antd/es/layout/layout";
import { getNumOfAcceptedTrainerBookings, getNumOfMyTrainingSessions, getNumOfPendingTrainerBookings, getNumOfTrainerBookings } from "../../utils/api";

export default function HomePage() {
    // const {
    //     token: { colorBgContainer, borderRadiusLG },
    // } = theme.useToken()

    const { data: numOfMyTrainingSessions } = useQuery({
        queryKey: ["num-of-my-training-sessions"],
        queryFn: () => getNumOfMyTrainingSessions()
    })

    const { data: numOfTrainerBookings } = useQuery({
        queryKey: ["num-of-trainer-bookings"],
        queryFn: () => getNumOfTrainerBookings()
    })

    const { data: numOfPendingTrainerBookings } = useQuery({
        queryKey: ["num-of-pending-trainer-bookings"],
        queryFn: () => getNumOfPendingTrainerBookings()
    })

    const { data: numOfAcceptedTrainerBookings } = useQuery({
        queryKey: ["num-of-accepted-trainer-bookings"],
        queryFn: () => getNumOfAcceptedTrainerBookings()
    })

    return (
        // <Content
        //     style={{
        //         margin: '24px 16px',
        //         padding: 24,
        //         minHeight: 280,
        //         background: colorBgContainer,
        //         borderRadius: borderRadiusLG,
        //     }}
        // >
        //     Content
        // </Content>
        <>
            <Row gutter={16}>
                <Col span={12}>
                    <Statistic title="Number of training sessions" value={numOfMyTrainingSessions} />
                </Col>
                <Col span={12}>
                    <Statistic title="Number of training bookings" value={numOfTrainerBookings} />
                </Col>
                <Col span={12}>
                    <Statistic title="Number of pending training bookings" value={numOfPendingTrainerBookings} suffix={`/${numOfTrainerBookings}`}/>
                </Col>
                <Col span={12}>
                    <Statistic title="Number of accepted training bookings" value={numOfAcceptedTrainerBookings} suffix={` /${numOfTrainerBookings}`}/>
                </Col>
            </Row>
        </>
    )
}