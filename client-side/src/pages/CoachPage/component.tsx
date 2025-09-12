import { Table, Tag, theme, Typography } from "antd";
import { Content } from "antd/es/layout/layout";
import type { UserRequest } from "../../utils/types/user/User";
import { getCoaches } from "../../utils/api";
import { useQuery } from "@tanstack/react-query";

export default function CoachPage() {
    const {
        token: { colorBgContainer, borderRadiusLG },
    } = theme.useToken()


    const { data: coaches = [], isLoading } = useQuery<UserRequest[], Error>({
        queryKey: ["coaches"],
        queryFn: getCoaches
    })

    const columns = [
        { title: "First name", dataIndex: "firstName", key: "firstName" },
        { title: "Last name", dataIndex: "lastName", key: "lastName" },
        { title: "Username", dataIndex: "username", key: "username" },
        { title: "Email", dataIndex: "email", key: "email" },
        {
            title: "Role",
            dataIndex: "role",
            key: "role",
            render: (role: string) => {
                let color = role === "coach" ? "blue" : "cyan"
                return <Tag color={color}>{role.toUpperCase()}</Tag>
            }
        }
    ]

    const { Title } = Typography

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
            <Title>
                Coaches
            </Title>
            <Table
                dataSource={coaches}
                columns={columns}
                rowKey="id"
                loading={isLoading}
                pagination={{ pageSize: 5 }}
            />
        </Content>
    )
}