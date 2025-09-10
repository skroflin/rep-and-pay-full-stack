import { CopyrightOutlined } from "@ant-design/icons";
import { Flex, Typography } from "antd";

const { Text } = Typography

export default function Footer() {
    return (
        <Flex justify="center" align="center" vertical>
            <Text strong italic style={{ color: "darkgray" }}>
                Rep & Pay
            </Text>
            <Text strong style={{ color: "darkgray" }}>
                <CopyrightOutlined />2025
            </Text>
        </Flex>
    )
}