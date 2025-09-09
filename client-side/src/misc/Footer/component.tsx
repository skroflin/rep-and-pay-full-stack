import { CopyrightOutlined } from "@ant-design/icons";
import { Flex, Typography } from "antd";

const { Text } = Typography

export default function Footer() {
    return (
        <Flex justify="center" align="center">
            <Text strong italic style={{ color: "darkgray" }}>
                Rep & Pay <CopyrightOutlined />
            </Text>
        </Flex>
    )
}