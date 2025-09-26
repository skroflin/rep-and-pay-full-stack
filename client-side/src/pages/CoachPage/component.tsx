import { Divider, Input, Table, Tag, theme, Typography } from "antd";
import { Content } from "antd/es/layout/layout";
import type { UserRequest } from "../../utils/types/user/User";
import { getCoaches } from "../../utils/api";
import { useQuery } from "@tanstack/react-query";
import { getCoachBySearchTerm } from "../../utils/api";
import { useState } from "react";
import { MailOutlined, TeamOutlined } from "@ant-design/icons";

export default function CoachPage() {
    const {
        token: { colorBgContainer, borderRadiusLG },
    } = theme.useToken()

    const [searchTerm, setSearchTerm] = useState("")

    const { data: coaches = [], isLoading } = useQuery<UserRequest[], Error>({
        queryKey: ["coaches"],
        queryFn: getCoaches
    })

    const { data: filteredCoaches = [], isLoading: isFilteredLoading } = useQuery<UserRequest[], Error>({
        queryKey: ["user-by-search-term", searchTerm],
        queryFn: () => getCoachBySearchTerm(searchTerm),
        enabled: searchTerm.length > 0
    })

    const { Title, Text } = Typography

    const columns = [
        { title: "First name", dataIndex: "firstName", key: "firstName" },
        { title: "Last name", dataIndex: "lastName", key: "lastName" },
        { title: "Username", dataIndex: "username", key: "username" },
        { title: <><Text>Email<MailOutlined style={{ marginLeft: 5 }} /></Text></>, dataIndex: "email", key: "email" },
        {
            title: <><Text>Role<TeamOutlined style={{ marginLeft: 5 }} /></Text></>,
            dataIndex: "role",
            key: "role",
            render: (role: string) => {
                let color = role === "coach" ? "blue" : "cyan"
                return <Tag color={color}>{role.toUpperCase()}</Tag>
            }
        }
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
            <Title>
                Coaches
            </Title>
            <Input.Search placeholder="Search for coaches"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                loading={isFilteredLoading}
                allowClear
                style={{
                    marginBottom: 40
                }}
            />

            <Divider style={{ marginTop: 0 }} />

            {coaches.length === 0 ? (
                <Text>
                    There are no coaches in the system.
                </Text>
            ) : (
                <Table
                    dataSource={searchTerm.length > 0 ? filteredCoaches : coaches}
                    columns={columns}
                    rowKey="id"
                    loading={searchTerm.length > 0 ? isFilteredLoading : isLoading}
                    pagination={{ pageSize: 5 }}
                />
            )}

        </Content>
    )
}