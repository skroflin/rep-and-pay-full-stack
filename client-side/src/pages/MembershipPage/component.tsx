import { useMutation, useQuery } from "@tanstack/react-query";
import { Button, Col, Divider, Flex, Select, Spin, Table, Tag, theme, Typography } from "antd";
import { Content } from "antd/es/layout/layout";
import type { CheckoutRequest } from "../../utils/types/Checkout";
import { createCheckoutSession, confirmPayment, getMyMemberships, getMonthOptions } from "../../utils/api";
import { toast } from "react-toastify";
import { useLocation, useNavigate } from "react-router";
import { useEffect, useState } from "react";
import { DollarOutlined, OrderedListOutlined } from "@ant-design/icons";
import type { Membership } from "../../utils/types/Membership";
import dayjs from "dayjs";
import type { MonthOptionResponse } from "../../utils/types/MonthOptions";

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

    const { data: monthOptions, isLoading: monthOptionsLoading } = useQuery<MonthOptionResponse[]>({
        queryKey: ["month-options"],
        queryFn: () => getMonthOptions()
    })

    const checkoutMutation = useMutation({
        mutationFn: (req: CheckoutRequest) => createCheckoutSession(req),
        onSuccess: (checkoutUrl) => {
            if (checkoutUrl) {
                toast.info("Redirecting you to payment site...")
                setTimeout(() => {
                    window.location.href = checkoutUrl
                }, 1500)
            } else {
                toast.error("Unable to create checkout session!")
            }
        },
        onError: () => {
            toast.error("Error upon creating checkout session!")
        }
    })

    const confirmMutation = useMutation({
        mutationFn: (status: string) => confirmPayment(status),
        onSuccess: () => {
            if (status == "success") {
                toast.success("Payment successful!")
                navigate("/")
            } else if (status === "cancel") {
                toast.error("Payment cancelled!")
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

    // Primijeniti da se može platiti za trenutni mjesec i iduća dva mjeseca

    // const months = [
    //     "January", "February", "March", "April", "May", "June", "July", "August",
    //     "September", "October", "November", "December"
    // ]

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
        { title: "Membership price", dataIndex: "membershipPrice", key: "membershipPrice", render: (membershipPrice: number) => <Tag color="green" style={{ float: "right" }}>{membershipPrice}</Tag> },
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
                            {upcomingMonths.map((m) => (
                                <Select.Option key={m.value} value={m.value}>
                                    {m.label}
                                </Select.Option>
                            ))}
                        </Select>
                        {checkoutMutation.isPending || confirmMutation.isPending ? (
                            <Spin tip="Loading..." />
                        ) : (
                            <Button
                                type="primary"
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
                        <Spin />
                    ) : (
                        <Table
                            columns={columns}
                            dataSource={memberships || []}
                            rowKey="id"
                            pagination={{ pageSize: 2 }}
                            scroll={{ x: true }}
                            style={{ width: "100%" }}
                        />
                    )}
                </Flex>
            </Col>
        </Content>
    )
}