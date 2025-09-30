import { useMutation, useQuery } from "@tanstack/react-query";
import { Button, Col, Divider, Flex, Select, Spin, Table, Tag, theme, Typography } from "antd";
import { Content } from "antd/es/layout/layout";
import type { CheckoutRequest } from "../../utils/types/Checkout";
import { createCheckoutSession, confirmPayment, getMyMemberships } from "../../utils/api";
import { toast } from "react-toastify";
import { useLocation, useNavigate } from "react-router";
import { useEffect, useState } from "react";
import { DollarOutlined, LoadingOutlined, OrderedListOutlined } from "@ant-design/icons";
import type { Membership } from "../../utils/types/Membership";
import dayjs from "dayjs";

export default function MembershipPage() {
    const {
        token: { colorBgContainer, borderRadiusLG },
    } = theme.useToken()

    const location = useLocation()
    const navigate = useNavigate()
    const queryParams = new URLSearchParams(location.search)
    const status = queryParams.get("status")
    const [selectedMonth, setSelectedMonth] = useState<number | null>(null)

    const { data: memberships, isLoading: membershipLoading } = useQuery<Membership[]>({
        queryKey: ["memberships"],
        queryFn: () => getMyMemberships()
    })

    const checkoutMutation = useMutation({
        mutationFn: (req: CheckoutRequest) => createCheckoutSession(req),
        onSuccess: (checkoutUrl) => {
            if (checkoutUrl) {
                toast.info("Redirecting you to payment site...", {position: "top-center"})
                setTimeout(() => {
                    window.location.href = checkoutUrl
                }, 1500)
            } else {
                toast.error("Unable to create checkout session!", {position: "top-center"})
            }
        },
        onError: () => {
            toast.error("Error upon creating checkout session!", {position: "top-center"})
        }
    })

    const confirmMutation = useMutation({
        mutationFn: (status: string) => confirmPayment(status),
        onSuccess: () => {
            if (status == "success") {
                toast.success("Payment successful!", {position: "top-center"})
                navigate("/")
            } else if (status === "cancel") {
                toast.error("Payment cancelled!", {position: "top-center"})
            }
        },
        onError: () => toast.error("Error upon pay confirmation!")
    })

    useEffect(() => {
        if (status) {
            confirmMutation.mutate(status)
        }
    }, [status])

    const { Text, Title } = Typography

    const getNextThreeMonths = () => {
        const now = dayjs()
        return Array.from({ length: 3 }, (_, i) => {
            const month = now.add(i, "month")
            return {
                label: month.format("MMMM"),
                value: month.month() + 1
            }
        })
    }

    const upcomingMonths = getNextThreeMonths()

    const columns = [
        { title: "Start Date", dataIndex: "startDate", key: "startDate", render: (date: Date | string) => dayjs(date).format("DD.MM.YYYY") },
        { title: "End Date", dataIndex: "endDate", key: "endDate", render: (date: Date | string) => dayjs(date).format("DD.MM.YYYY") },
        { title: "Membership price", dataIndex: "membershipPrice", key: "membershipPrice", render: (membershipPrice: number) => <Tag color="green" style={{ float: "right" }}>{`${(membershipPrice / 100).toFixed(2)} EUR`}</Tag> },
        { title: "Payment Date", dataIndex: "paymentDate", key: "paymentDate", render: (date: Date | string) => dayjs(date).format("DD.MM.YYYY") },
    ]

    return (
        <Content
            style={{
                margin: '24px 16px',
                padding: 24,
                minHeight: 280,
                background: colorBgContainer,
                borderRadius: borderRadiusLG,
            }}
        >
            <Flex
                justify="center"
                align="center"
                wrap
                gap={32}
                style={{ minHeight: 280 }}
            >
                <Col xs={24} sm={20} md={12} lg={8} style={{ textAlign: "center", marginBottom: 24 }}>
                    <Title level={3}>Membership</Title>
                    <Text strong>Choose your membership month and continue via Stripe</Text>
                </Col>
                <Col xs={24} sm={20} md={12} lg={8} style={{ textAlign: "center" }}>
                    <Flex vertical gap={16} align="center">
                        <Select
                            placeholder="Select month"
                            style={{ width: 200 }}
                            onChange={(val) => setSelectedMonth(val)}
                            prefix={<OrderedListOutlined />}
                        >
                            {upcomingMonths.map((m) => {
                                const isPaid = memberships?.some(mem => dayjs(mem.startDate).month() + 1 === m.value && mem.alreadyPaid)

                                return (
                                    <Select.Option key={m.value} value={m.value} disabled={isPaid}>
                                        {m.label} {isPaid && <Tag color="red">Already paid</Tag>}
                                    </Select.Option>
                                )
                            })}
                        </Select>
                        {checkoutMutation.isPending || confirmMutation.isPending ? (
                            <Spin tip="Loading..." />
                        ) : (
                            <Button
                                variant="solid"
                                color="green"
                                size="middle"
                                onClick={() => checkoutMutation.mutate({ price: 3000, month: selectedMonth! })}
                                icon={<DollarOutlined />}
                                disabled={selectedMonth === null}
                                style={{ width: 200 }}
                            >
                                Pay for membership
                            </Button>
                        )}
                    </Flex>
                </Col>
            </Flex>
            <Divider />
            <Col xs={24} sm={20} md={16} lg={12} style={{ margin: "0 auto", marginTop: 32 }}>
                <Flex vertical gap={16} align="center">
                    <Title level={4}>
                        Payment history
                    </Title>
                    {membershipLoading ? (
                        <Flex align="center" justify="center" vertical>
                            <Spin
                                indicator={<LoadingOutlined style={{ color: "black", fontSize: 48 }} spin />}
                                style={{ fontSize: 64 }}
                            />
                            <Title level={4} style={{ marginTop: 16 }}>Loading memberships...</Title>
                        </Flex>
                    ) : memberships && memberships.length > 0 ? (
                        <Table
                            columns={columns}
                            dataSource={memberships || []}
                            rowKey="id"
                            pagination={{ pageSize: 2 }}
                            scroll={{ x: true }}
                            style={{ width: "100%" }}
                        />
                    ) : (
                        <Text>No memberships</Text>
                    )}
                </Flex>
            </Col>
        </Content>
    )
}