import { Button, List, Modal, Space, Table, Tag, theme, Typography } from "antd";
import { Content } from "antd/es/layout/layout";
import { useState } from "react";
import type { UserRequest } from "../../utils/types/user/User";
import { getMembershipByUser, getRegularUsers } from "../../utils/api";
import { toast } from "react-toastify";
import type { Membership } from "../../utils/types/Membership";
import { useMutation, useQuery } from "@tanstack/react-query";
import dayjs from "dayjs";
import type { AxiosError } from "axios";

export default function UserPage() {
    const {
        token: { colorBgContainer, borderRadiusLG },
    } = theme.useToken()

    const [isModalOpen, setIsModalOpen] = useState(false)
    const [selectedUser, setSelectedUser] = useState<UserRequest | null>(null)
    const [memberships, setMemberships] = useState<Membership[]>([])

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
                let color = role === "user" ? "cyan" : "blue"
                return <Tag color={color}>{role.toUpperCase()}</Tag>
            }
        },
        {
            title: "Functions",
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
            <Title>
                Users
            </Title>
            <Table
                dataSource={users}
                columns={columns}
                rowKey="id"
                loading={isLoading}
                pagination={{ pageSize: 5 }}
            />

            <Modal
                title={`Selected membership for ${selectedUser?.firstName} ${selectedUser?.lastName}`}
                open={isModalOpen}
                onOk={() => setIsModalOpen(false)}
                onCancel={() => setIsModalOpen(false)}
            >
                {memberships.length > 0 ? (
                    <List bordered>
                        {memberships.map((m, idx) => (
                            <List.Item key={idx}>
                                <Text>
                                    Start: <Tag color="green">{dayjs(m.startDate).format("DD.MM.YYYY")}</Tag>
                                </Text>
                                <Text>
                                    End: <Tag color="orange">{dayjs(m.endDate).format("DD.MM.YYYY")}</Tag>
                                </Text>
                                <Text>
                                    Price: <Tag color="green">{(m.membershipPrice / 100).toFixed(2)} EUR</Tag>
                                </Text>
                                <Text>
                                    Paid: <Tag color="orange">{dayjs(m.paymentDate).format("DD.MM.YYYY")}</Tag>
                                </Text>
                            </List.Item>
                        ))}
                    </List>
                ) : (
                    <Text>No memberships found</Text>
                )}
            </Modal>
        </Content>
    )
}