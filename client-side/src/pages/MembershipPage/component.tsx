import { useMutation } from "@tanstack/react-query";
import { Button, Col, Row, theme, Typography } from "antd";
import { Content } from "antd/es/layout/layout";
import type { CheckoutRequest } from "../../utils/types/Checkout";
import { createCheckoutSession } from "../../utils/api";
import { toast } from "react-toastify";

export default function MembershipPage() {
    const {
        token: { colorBgContainer, borderRadiusLG },
    } = theme.useToken()

    const mutation = useMutation({
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

    const handleBuyMembership = () => {
        mutation.mutate({ price: 3000 })
    }

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
                    <Button
                        type="primary"
                        size="large"
                        loading={mutation.isPending}
                        onClick={handleBuyMembership}
                    >
                        Pay for membership
                    </Button>
                </Col>
            </Row>
        </Content>
    )
}