import { useMutation } from "@tanstack/react-query";
import { Button, Col, Row, Spin, theme, Typography } from "antd";
import { Content } from "antd/es/layout/layout";
import type { CheckoutRequest } from "../../utils/types/Checkout";
import { createCheckoutSession, confirmPayment } from "../../utils/api";
import { toast } from "react-toastify";
import { useLocation, useNavigate } from "react-router";
import { useEffect } from "react";

export default function MembershipPage() {
    const {
        token: { colorBgContainer, borderRadiusLG },
    } = theme.useToken()

    const location = useLocation()
    const navigate = useNavigate()
    const queryParams = new URLSearchParams(location.search)
    const status = queryParams.get("status")

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
                    <Text>Choose your membership and continue via Stripe</Text>
                </Col>
                <Col>
                    {checkoutMutation.isPending || confirmMutation.isPending ? (
                        <Spin tip="Loading..." />
                    ) : (
                        <Button
                            type="primary"
                            size="large"
                            onClick={() => checkoutMutation.mutate({price: 3000})}
                        >
                            Pay for membership
                        </Button>
                    )}
                </Col>
            </Row>
        </Content>
    )
}