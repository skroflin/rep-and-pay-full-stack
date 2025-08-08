import { Calendar } from "antd";
import { toast } from "react-toastify";

export default function TrainingSessionPage() {    
    return (
        <div
            style={{
                display: "block",
                width: 700,
                padding: 30
            }}
        >
            <Calendar
                onChange={(value) => {
                    toast.success(`You selected ${value.format('DD.MM.YYYY')}`)
                }} 
            />
        </div>
    )
}