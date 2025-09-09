import { useMutation, useQuery } from "@tanstack/react-query";
import { Button, Col, Flex, Select, Spin, Table, theme, Typography } from "antd";
import { Content } from "antd/es/layout/layout";
import type { CheckoutRequest } from "../../utils/types/Checkout";
import { createCheckoutSession, confirmPayment, getMyMemberships } from "../../utils/api";
import { toast } from "react-toastify";
import { useLocation, useNavigate } from "react-router";
import { useEffect, useState } from "react";
import { DollarOutlined, OrderedListOutlined } from "@ant-design/icons";
import type { Membership } from "../../utils/types/Membership";

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
                toast.info("Redirecting you to payment site...")
                setTimeout(() => {
                    window.location.href = checkoutUrl
                }, 1500)
                console.log(checkoutUrl)
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

    const months = [
        "January", "February", "March", "April", "May", "June", "July", "August",
        "September", "October", "November", "December"
    ]

    const columns = [
        { title: "Start Date", dataIndex: "startDate", key: "startDate" },
        { title: "End Date", dataIndex: "endDate", key: "endDate" },
        { title: "Membership price", dataIndex: "membershipPrice", key: "membershipPrice" },
        { title: "Payment Date", dataIndex: "paymentDate", key: "paymentDate" },
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
                            {months.map((m, idx) => (
                                <Select.Option key={idx + 1} value={idx + 1}>
                                    {m}
                                </Select.Option>
                            ))}
                        </Select>
                        {checkoutMutation.isPending || confirmMutation.isPending ? (
                            <Spin tip="Loading..." />
                        ) : (
                            <Button
                                type="primary"
                                size="middle"
                                onClick={() => checkoutMutation.mutate({ price: 3000, month: selectedMonth })}
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
                            pagination={{ pageSize: 5 }}
                            scroll={{ x: true }}
                            style={{ width: "100%" }}
                        />
                    )}
                </Flex>
            </Col>
        </Content>
    )
}