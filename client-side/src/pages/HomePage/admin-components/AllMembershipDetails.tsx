import { useQuery } from "@tanstack/react-query";
import type { MembershipResponse } from "../../../utils/types/Membership";
import { getAllMemberships } from "../../../utils/api";
import { getRole } from "../../../utils/helper";
import { useEffect, useState } from "react";
import { Button, Descriptions, Drawer, Flex, Space, Spin, Tag, Typography } from "antd";
import { ArrowLeftOutlined, ArrowRightOutlined, ContainerOutlined, LoadingOutlined } from "@ant-design/icons";
import dayjs from "dayjs";

export default function AllMembershipDetails({
    open,
    onClose
}: {
    open: boolean
    onClose: () => void
}) {

    const role = getRole()

    const { data: memberships, isLoading: allMembershipsLoading } = useQuery<MembershipResponse[]>({
        queryKey: ["all-memberships"],
        queryFn: getAllMemberships,
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
                    <Title level={4} style={{ textAlign: "center", width: "100%" }}>
                        All memberships <ContainerOutlined />
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
            {allMembershipsLoading ? (
                <Flex align="center" justify="center" vertical>
                    <Spin
                        indicator={<LoadingOutlined style={{ color: "black", fontSize: 48 }} spin />}
                        style={{ fontSize: 64 }}
                    />
                    <Title level={4} style={{ marginTop: 16 }}>Loading all memberships...</Title>
                </Flex>
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
                            <Tag color="volcano">{dayjs(membership.endDate).format("DD.MM.YYYY")}</Tag>
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