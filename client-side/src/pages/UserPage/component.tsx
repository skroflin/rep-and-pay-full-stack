import { Button, Modal, Space, Table, Tag, theme } from "antd";
import { Content } from "antd/es/layout/layout";
import { useState } from "react";
import type { UserRequest } from "../../utils/types/User";
import { getMembershipByUser, getRegularUsers } from "../../utils/api";
import { toast } from "react-toastify";
import type { Membership } from "../../utils/types/Membership";
import { useMutation, useQuery } from "@tanstack/react-query";
import dayjs from "dayjs";

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
        onError: (err: any, username) => {
            toast.error(`Error fetching memberships for user ${username}: ${err.message}`)
        }
    })

    const { data: users = [], isLoading } = useQuery<UserRequest[], Error>({
        queryKey: ["users"],
        queryFn: getRegularUsers
    })

    const columns = [
        { title: "First name", dataIndex: "firstName", key: "firstName" },
        { title: "Last name", dataIndex: "lastName", key: "lastName" },
        { title: "Name", dataIndex: "firstName", key: "firstName" },
        { title: "Username", dataIndex: "username", key: "username" },
        { title: "Email", dataIndex: "email", key: "email" },
        {
            title: "Role",
            dataIndex: "role",
            key: "role",
            render: (role: string) => {
                let color = role === "user" ? "green" : "blue"
                return <Tag color={color}>{role.toUpperCase()}</Tag>
            }
        },
        {
            title: "Membership",
            dataIndex: "isMembershipPaid",
            key: "isMembershipPaid",
            render: (paid: boolean) => (paid ? "Paid" : "Not paid")
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
            <h2>
                Users
            </h2>
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
                onCancel={() => setIsModalOpen(false)}
            >
                {memberships.length > 0 ? (
                    <ul>
                        {memberships.map((m, idx) => (
                            <li key={idx}>
                                Start: {dayjs(m.startDate).format("DD.MM.YYYY")}, end: {dayjs(m.endDate).format("DD.MM.YYYY")}, price: {m.membershipPrice}
                            </li>
                        ))}
                    </ul>
                ) : (
                    <p>No memberships found</p>
                )}
            </Modal>
        </Content>
    )
}