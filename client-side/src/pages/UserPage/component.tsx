import { Button, Divider, Input, List, Modal, Space, Table, Tag, theme, Typography } from "antd";
import { Content } from "antd/es/layout/layout";
import { useState } from "react";
import type { UserRequest } from "../../utils/types/user/User";
import { getMembershipByUser, getRegularUsers, getUserBySearchTerm } from "../../utils/api";
import { toast } from "react-toastify";
import type { Membership } from "../../utils/types/Membership";
import { useMutation, useQuery } from "@tanstack/react-query";
import dayjs from "dayjs";
import type { AxiosError } from "axios";
import { MailOutlined, TeamOutlined, ToolOutlined } from "@ant-design/icons";

export default function UserPage() {
    const {
        token: { colorBgContainer, borderRadiusLG },
    } = theme.useToken()

    const [isModalOpen, setIsModalOpen] = useState(false)
    const [selectedUser, setSelectedUser] = useState<UserRequest | null>(null)
    const [memberships, setMemberships] = useState<Membership[]>([])
    const [searchTerm, setSearchTerm] = useState("")

    const membershipMutation = useMutation({
        mutationFn: (userId: string) => getMembershipByUser(userId),
        onSuccess: (data, userId) => {
            setMemberships(data)
            setSelectedUser(users.find((u) => u.id == userId) || null)
            setIsModalOpen(true)
        },
        onError: (err: AxiosError, userId: string) => {
            const user = users.find(u => u.id === userId);
            const username = user?.username ?? userId;
            if (err.response?.status === 400) {
                toast.warning(`User ${username} has no memberships`);
            } else if (err.response?.status === 500) {
                toast.error(`Server error while fetching memberships for user ${username}`);
            } else {
                toast.error(`Error fetching memberships for user ${username}: ${err.message}`);
            }
        }
    })

    const { data: users = [], isLoading } = useQuery<UserRequest[], Error>({
        queryKey: ["users"],
        queryFn: getRegularUsers
    })

    const { Text, Title } = Typography

    const { data: filteredUsers = [], isLoading: isFilteredLoading } = useQuery<UserRequest[], Error>({
        queryKey: ["user-by-search-term", searchTerm],
        queryFn: () => getUserBySearchTerm(searchTerm),
        enabled: searchTerm.length > 0
    })

    const columns = [
        { title: "First name", dataIndex: "firstName", key: "firstName" },
        { title: "Last name", dataIndex: "lastName", key: "lastName" },
        { title: "Username", dataIndex: "username", key: "username" },
        { title: <><Text>Mail</Text><MailOutlined style={{ marginLeft: 5 }} /></>, dataIndex: "email", key: "email" },
        {
            title: <><Text>Role</Text><TeamOutlined style={{ marginLeft: 5 }} /></>,
            dataIndex: "role",
            key: "role",
            render: (role: string) => {
                let color = role === "user" ? "cyan" : "blue"
                return <Tag color={color}>{role.toUpperCase()}</Tag>
            }
        },
        {
            title: <><Text>Functions</Text><ToolOutlined style={{ marginLeft: 5 }} /></>,
            key: "functions",
            render: (_: any, record: UserRequest) => (
                <Space>
                    <Button
                        type="link"
                        onClick={() => membershipMutation.mutate(record.id)}
                        loading={membershipMutation.isPending && selectedUser?.id === record.id}
                    >
                        View memberships
                    </Button>
                </Space>
            )
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
                Users
            </Title>
            <Input.Search placeholder="Search for users"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                loading={isFilteredLoading}
                allowClear
                style={{
                    marginBottom: 40
                }}
            />

            <Divider style={{ marginTop: 0 }} />

            {users.length === 0 ? (
                <Text>No users found</Text>
            ) : (
                <Table
                    dataSource={searchTerm.length > 0 ? filteredUsers : users}
                    columns={columns}
                    rowKey="id"
                    loading={searchTerm.length > 0 ? isFilteredLoading : isLoading}
                    pagination={{ pageSize: 5 }}
                />
            )
            }

            <Modal
                title={`Selected membership for ${selectedUser?.firstName} ${selectedUser?.lastName}`}
                open={isModalOpen}
                onOk={() => setIsModalOpen(false)}
                onCancel={() => setIsModalOpen(false)}
            >
                {memberships.length > 0 ? (
                    <List bordered>
                        {memberships.map((m, idx) => (
                            <>
                                <Title style={{ marginLeft: 10 }} level={5}>{dayjs(m.startDate).format("MMMM - YYYY.")}</Title>
                                <Divider style={{ margin: 0 }} />
                                <List.Item key={idx}>
                                    <Text>
                                        Start: <Tag>{dayjs(m.startDate).format("DD.MM.YYYY.")}</Tag>
                                    </Text>
                                    <Text>
                                        End: <Tag color="volcano">{dayjs(m.endDate).format("DD.MM.YYYY.")}</Tag>
                                    </Text>
                                    <Text>
                                        Price: <Tag color="green">{(m.membershipPrice / 100).toFixed(2)} EUR</Tag>
                                    </Text>
                                    <Text>
                                        Paid: <Tag>{dayjs(m.paymentDate).format("DD.MM.YYYY.")}</Tag>
                                    </Text>
                                </List.Item>
                            </>
                        ))}
                    </List>
                ) : (
                    <Text>No memberships found</Text>
                )}
            </Modal>
        </Content>
    )
}