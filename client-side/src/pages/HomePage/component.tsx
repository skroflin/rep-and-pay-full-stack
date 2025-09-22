import { useQueries } from "@tanstack/react-query";
import { Button, Card, Divider, Flex, Statistic } from "antd";
import {
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
import { useState } from "react";
import ActiveMembershipDetails from "./admin-components/ActiveMembershipDetails";
import ExpiredMembershipDetails from "./admin-components/ExpiredMembershipDetails";
import AllMembershipDetails from "./admin-components/AllMembershipDetails";
import UserTrainingSessionDetails from "./user-components/UserTrainingSessionDetails";
import UserBookingDetails from "./user-components/UserBookingDetails";

export default function HomePage() {

    const role = getRole()

    const [activeDrawerOpen, setActiveDrawerOpen] = useState(false)
    const [expiredDrawerOpen, setExpiredDrawerOpen] = useState(false)
    const [membershipsDrawerOpen, setMembershipsDrawerOpen] = useState(false)

    const [userTrainingsDrawerOpen, setUserTrainingDrawerOpen] = useState(false)
    const [userBookingsDrawerOpen, setUserBookingsDrawerOpen] = useState(false)

    const openActiveDrawer = () => {
        setExpiredDrawerOpen(false)
        setActiveDrawerOpen(true)
        setMembershipsDrawerOpen(false)
    }

    const openExpiredDrawer = () => {
        setExpiredDrawerOpen(true)
        setActiveDrawerOpen(false)
        setMembershipsDrawerOpen(false)
    }

    const openMembershipDrawer = () => {
        setExpiredDrawerOpen(false)
        setActiveDrawerOpen(false)
        setMembershipsDrawerOpen(true)
    }

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
                queryKey: ["num-of-user-bookings"],
                queryFn: getNumOfMyBookings,
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

    const [
        numOfMyTrainingSessions,
        numOfTrainerBookings,
        numOfPendingTrainerBookings,
        numOfAcceptedTrainerBookings,
        numOfBeginnerTrainingSessions,
        numOfIntermediateTrainingSessions,
        numOfUserTrainingSessions,
        numOfMyBookings,
        numOfAdvancedTrainingSessions,
        numOfMemberships,
        numOfActiveMemberships,
        numOfExpiredMemberships
    ] = results.map(r => Number(r.data ?? 0));

    return (
        <>
            <Flex
                wrap="wrap"
                gap={32}
                justify="center"
                align="center"
                style={{ marginTop: 30, minHeight: "80vh", maxWidth: 1200, marginLeft: "auto", marginRight: "auto" }}
            >
                {role === "coach" && (
                    <>
                        <Card variant="borderless" style={{ minWidth: 280, maxWidth: 350, flex: "1 1 320px" }}>
                            <Statistic
                                title="Number of training bookings"
                                value={numOfTrainerBookings}
                                formatter={value => <CountUp end={Number(value)} duration={2} />}
                            />
                        </Card>
                        <Card variant="borderless" style={{ minWidth: 280, maxWidth: 350, flex: "1 1 320px" }}>
                            <Statistic
                                title="Number of pending booking requests"
                                value={numOfPendingTrainerBookings}
                                suffix={`/${numOfTrainerBookings}`}
                                formatter={value => <CountUp end={Number(value)} duration={2} />}
                            />
                        </Card>
                        <Card variant="borderless" style={{ minWidth: 280, maxWidth: 350, flex: "1 1 320px" }}>
                            <Statistic
                                title="Number of accepted booking requests"
                                value={numOfAcceptedTrainerBookings}
                                suffix={`/${numOfTrainerBookings}`}
                                formatter={value => <CountUp end={Number(value)} duration={2} />}
                            />
                        </Card>
                        <Divider />
                        <Card variant="borderless" style={{ minWidth: 280, maxWidth: 350, flex: "1 1 320px" }}>
                            <Statistic
                                title="Number of training sessions"
                                value={numOfMyTrainingSessions}
                                formatter={value => <CountUp end={Number(value)} duration={2} />}
                            />
                        </Card>
                        <Card variant="borderless" style={{ minWidth: 280, maxWidth: 350, flex: "1 1 320px" }}>
                            <Statistic
                                title="Number of beginner training sessions"
                                value={numOfBeginnerTrainingSessions}
                                suffix={` /${numOfMyTrainingSessions}`}
                                formatter={value => <CountUp end={Number(value)} duration={2} />}
                            />
                        </Card>
                        <Card variant="borderless" style={{ minWidth: 280, maxWidth: 350, flex: "1 1 320px" }}>
                            <Statistic
                                title="Number of intermediate training sessions"
                                value={numOfIntermediateTrainingSessions}
                                suffix={` /${numOfMyTrainingSessions}`}
                                formatter={value => <CountUp end={Number(value)} duration={2} />}
                            />
                        </Card>
                        <Card variant="borderless" style={{ minWidth: 280, maxWidth: 350, flex: "1 1 320px" }}>
                            <Statistic
                                title="Number of advanced training sessions"
                                value={numOfAdvancedTrainingSessions}
                                suffix={` /${numOfMyTrainingSessions}`}
                                formatter={value => <CountUp end={Number(value)} duration={2} />}
                            />
                        </Card>
                    </>
                )}
                {role === "user" && (
                    <>
                        <Card
                            variant="borderless"
                            style={{ minWidth: 280, maxWidth: 350, flex: "1 1 320px" }}
                            extra={<Button type="link" onClick={() => setUserBookingsDrawerOpen(true)}>View details</Button>}
                        >
                            <Statistic
                                title="Number of my bookings"
                                value={numOfMyBookings ?? 0}
                                formatter={value => <CountUp end={Number(value)} duration={2} />}
                            />
                        </Card>
                        <Card
                            variant="borderless"
                            style={{ minWidth: 280, maxWidth: 350, flex: "1 1 320px" }}
                            extra={<Button type="link" onClick={() => setUserTrainingDrawerOpen(true)}>View details</Button>}
                        >
                            <Statistic
                                title="Number of my training sessions"
                                value={numOfUserTrainingSessions}
                                formatter={value => <CountUp end={Number(value)} duration={2} />}
                            />
                        </Card>
                    </>
                )}
                {role === "superuser" && (
                    <>
                        <Card
                            variant="borderless"
                            style={{ minWidth: 280, maxWidth: 350, flex: "1 1 320px" }}
                            extra={<Button type="link" onClick={openMembershipDrawer}>View details</Button>}
                        >
                            <Statistic
                                title="Number of memberships"
                                value={numOfMemberships ?? 0}
                                formatter={value => <CountUp end={Number(value)} duration={2} />}
                            />
                        </Card>
                        <Card
                            variant="borderless"
                            style={{ minWidth: 280, maxWidth: 350, flex: "1 1 320px" }}
                            extra={<Button type="link" onClick={openActiveDrawer}>View details</Button>}
                        >
                            <Statistic
                                title="Number of active memberships"
                                value={numOfActiveMemberships ?? 0}
                                suffix={` /${numOfMemberships}`}
                                formatter={value => <CountUp end={Number(value)} duration={2} />}
                            />
                        </Card>
                        <Card
                            variant="borderless"
                            style={{ minWidth: 280, maxWidth: 350, flex: "1 1 320px" }}
                            extra={<Button type="link" onClick={openExpiredDrawer}>View details</Button>}
                        >
                            <Statistic
                                title="Number of expired memberships"
                                value={numOfExpiredMemberships ?? 0}
                                suffix={` /${numOfMemberships}`}
                                formatter={value => <CountUp end={Number(value)} duration={2} />}
                            />
                        </Card>
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

            <AllMembershipDetails
                open={membershipsDrawerOpen}
                onClose={() => setMembershipsDrawerOpen(false)}
            />

            <UserTrainingSessionDetails
                open={userTrainingsDrawerOpen}
                onClose={() => setUserTrainingDrawerOpen(false)}
            />

            <UserBookingDetails
                open={userBookingsDrawerOpen}
                onClose={() => setUserBookingsDrawerOpen(false)}
            />
        </>
    )

}