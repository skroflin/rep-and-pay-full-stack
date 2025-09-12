import { useQuery } from "@tanstack/react-query";
import { getUserTrainingSessions } from "../../../utils/api";
import { getRole, getUsername } from "../../../utils/helper";
import { useEffect, useState } from "react";
import { Button, Descriptions, Drawer, Space, Spin, Tag, Typography } from "antd";
import { ArrowLeftOutlined, ArrowRightOutlined } from "@ant-design/icons";
import dayjs from "dayjs";
import type { UserTrainingSessionResponse } from "../../../utils/types/user-authenticated/UserTrainingSessions";

export default function UserTrainingSessionDetails({
    open,
    onClose
}: {
    open: boolean
    onClose: () => void
}) {

    const role = getRole()

    const { data: trainingSessions, isLoading: trainingSessionsLoading } = useQuery<UserTrainingSessionResponse[]>({
        queryKey: ["user-training-sessions"],
        queryFn: getUserTrainingSessions,
        enabled: role === "user"
    })

    const [currentIndex, setCurrentIndex] = useState(0)

    useEffect(() => {
        if (open) setCurrentIndex(0)
    }, [open, trainingSessions])

    if (!trainingSessions || trainingSessions.length === 0) return null

    const trainingSession = trainingSessions[currentIndex]

    const { Text, Title } = Typography

    const username = getUsername()

    return (
        <Drawer
            open={open}
            onClose={onClose}
            title={
                <span>
                    <Title level={4}>
                        Training sessions for {username}
                    </Title>
                    <Text style={{ float: "right" }}>
                        {currentIndex + 1} of {trainingSessions.length}
                    </Text>
                </span>
            }
            footer={
                <Space style={{ float: "right" }}>
                    <Button
                        disabled={currentIndex === 0}
                        onClick={() => setCurrentIndex((i) => i - 1)}
                        icon={
                            <ArrowLeftOutlined />
                        }
                    >
                        Back
                    </Button>
                    <Button
                        disabled={currentIndex === trainingSessions.length - 1}
                        onClick={() => setCurrentIndex((i) => i + 1)}
                        icon={
                            <ArrowRightOutlined />
                        }
                    >
                        Next
                    </Button>
                </Space>
            }
            placement="right"
        >
            {trainingSessionsLoading ? (
                <Spin />
            ) : (
                trainingSessions && (
                    <Descriptions
                        column={1}
                        bordered
                        size="small"
                    >
                        <Descriptions.Item label="Trainer">
                            <Text strong>{trainingSession.trainerFirstName} {trainingSession.trainerLastName}</Text>
                        </Descriptions.Item>
                        <Descriptions.Item label="Start of session">
                            <Text strong>{dayjs(trainingSession.startOfSession).format("HH:mm")}</Text>
                        </Descriptions.Item>
                        <Descriptions.Item label="End of session">
                            <Text strong>{dayjs(trainingSession.endOfSession).format("HH:mm")}</Text>
                        </Descriptions.Item>
                        <Descriptions.Item label="Training type">
                            <Tag color="blue">{trainingSession.trainingType.toUpperCase()}</Tag>
                        </Descriptions.Item>
                        <Descriptions.Item label="Training level">
                            <Tag color="geekblue">{trainingSession.trainingLevel.toUpperCase()}</Tag>
                        </Descriptions.Item>
                    </Descriptions>
                )
            )}
        </Drawer>
    )
}