import { useQuery } from "@tanstack/react-query";
import type { MembershipResponse } from "../../../utils/types/Membership";
import { getActiveMemberships } from "../../../utils/api";
import { getRole } from "../../../utils/helper";
import { useEffect, useState } from "react";
import { Button, Descriptions, Drawer, Space, Spin, Tag, Typography } from "antd";
import { ArrowLeftOutlined, ArrowRightOutlined } from "@ant-design/icons";
import dayjs from "dayjs";

export default function ActiveMembershipDetails({
    open,
    onClose
}: {
    open: boolean
    onClose: () => void
}) {

    const role = getRole()

    const { data: memberships, isLoading: activeMembershipsLoading } = useQuery<MembershipResponse[]>({
        queryKey: ["active-memberships"],
        queryFn: getActiveMemberships,
        enabled: role === "superuser"
    })

    const [currentIndex, setCurrentIndex] = useState(0)

    useEffect(() => {
        if (open) setCurrentIndex(0)
    }, [open, memberships])

    if (!memberships || memberships.length === 0) return null

    const membership = memberships[currentIndex]

    const { Text, Title } = Typography

    return (
        <Drawer
            open={open}
            onClose={onClose}
            title={
                <span>
                    <Title level={4}>
                        Active memberships
                    </Title>
                    <Text style={{ float: "right" }}>
                        {currentIndex + 1} of {memberships.length}
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
                        disabled={currentIndex === memberships.length - 1}
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
            {activeMembershipsLoading ? (
                <Spin />
            ) : (
                memberships && (
                    <Descriptions
                        column={1}
                        bordered
                        size="small"
                    >
                        <Descriptions.Item label="User">
                            <Text>{membership.firstName} {membership.lastName}</Text>
                        </Descriptions.Item>
                        <Descriptions.Item label="Start date for membership">
                            <Text>{dayjs(membership.startDate).format("DD.MM.YYYY")}</Text>
                        </Descriptions.Item>
                        <Descriptions.Item label="End date for membership">
                            <Text>{dayjs(membership.endDate).format("DD.MM.YYYY")}</Text>
                        </Descriptions.Item>
                        <Descriptions.Item label="Membership price">
                            <Tag color="green">{`${(membership.membershipPrice / 100).toFixed(2)} EUR`}</Tag>
                        </Descriptions.Item>
                        <Descriptions.Item label="Membership payment date">
                            <Text>{dayjs(membership.paymentDate).format("DD.MM.YYYY")}</Text>
                        </Descriptions.Item>
                    </Descriptions>
                )
            )}
        </Drawer>
    )
}