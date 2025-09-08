import { useMutation } from "@tanstack/react-query";
import { Button, Col, Flex, Row, Select, Spin, theme, Typography } from "antd";
import { Content } from "antd/es/layout/layout";
import type { CheckoutRequest } from "../../utils/types/Checkout";
import { createCheckoutSession, confirmPayment } from "../../utils/api";
import { toast } from "react-toastify";
import { useLocation, useNavigate } from "react-router";
import { useEffect, useState } from "react";
import { DollarOutlined, OrderedListOutlined } from "@ant-design/icons";

export default function MembershipPage() {
    const {
        token: { colorBgContainer, borderRadiusLG },
    } = theme.useToken()

    const location = useLocation()
    const navigate = useNavigate()
    const queryParams = new URLSearchParams(location.search)
    const status = queryParams.get("status")
    const [selectedMonth, setSelectedMonth] = useState<number | null>(null)

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
            <Row justify="center" align="middle" style={{ height: "100%" }}>
                <Col
                    style={{
                        textAlign: "center"
                    }}
                >
                    <Title>Membership</Title>
                    <Text>Choose your membership month and continue via Stripe</Text>
                </Col>
                <Col>
                    <Row>
                        <Select
                            placeholder="Select month"
                            style={{
                                width: 200
                            }}
                            onChange={(val) => setSelectedMonth(val)}
                            prefix={
                                <OrderedListOutlined />
                            }
                        >
                            {months.map((m, idx) => (
                                <Select.Option key={idx + 1} value={idx - 1}>
                                    {m}
                                </Select.Option>
                            ))}
                        </Select>
                    </Row>
                    <Flex
                        style={{
                            width: 200,
                            marginTop: 10
                        }}
                    >
                        {checkoutMutation.isPending || confirmMutation.isPending ? (
                            <Spin tip="Loading..." />
                        ) : (
                            <Button
                                type="primary"
                                size="middle"
                                onClick={() => checkoutMutation.mutate({ price: 3000, month: selectedMonth })}
                                icon={
                                    <DollarOutlined />
                                }
                            >
                                Pay for membership
                            </Button>
                        )}
                    </Flex>
                </Col>
            </Row>
        </Content>
    )
}