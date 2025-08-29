import { Table, theme } from "antd";
import { Content } from "antd/es/layout/layout";
import { useEffect, useState } from "react";
import type { UserRequest } from "../../utils/types/User";
import { getCoaches } from "../../utils/api";
import { toast } from "react-toastify";

export default function CoachPage() {
    const {
        token: { colorBgContainer, borderRadiusLG },
    } = theme.useToken()

    const [coaches, setCoaches] = useState<UserRequest[]>([])
    const [loading, setLoading] = useState<boolean>(true)

    useEffect(() => {
        getCoaches()
            .then((data) => setCoaches((data)))
            .catch((err) => toast.error(`Error upon fetching coaches: ${err}`))
            .finally(() => setLoading(false))
    }, [])

    const columns = [
        { title: "First name", dataIndex: "firstName", key: "firstName" },
        { title: "Last name", dataIndex: "lastName", key: "lastName" },
        { title: "Name", dataIndex: "firstName", key: "firstName" },
        { title: "Username", dataIndex: "username", key: "username" },
        { title: "Email", dataIndex: "email", key: "email" },
        { title: "Role", dataIndex: "role", key: "role" },
        {
            title: "Membership",
            dataIndex: "isMembershipPaid",
            key: "isMembershipPaid",
            render: (paid: boolean) => (paid ? "Paid" : "Not paid")
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
                Coaches
            </h2>
            <Table
                dataSource={coaches}
                columns={columns}
                rowKey="username"
                loading={loading}
                pagination={{ pageSize: 5 }}
            />
        </Content>
    )
}