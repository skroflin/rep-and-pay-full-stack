import { theme } from "antd";
import { Content } from "antd/es/layout/layout";

export default function MembershipPage() {
    const {
        token: { colorBgContainer, borderRadiusLG },
    } = theme.useToken()
    
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
            Content
        </Content>
    )
}