export function formatDate(dateString: string): string {
    const [year, month, day] = dateString.split("-");
    return `${day}.${month}.${year}`;
}

export function formatDateTime(dateString: string): string {
    const [datePart, timePart] = dateString.split("T");
    const [year, month, day] = datePart.split("-");
    const [hours, minutes] = timePart.split(":");
    return `${day}.${month}.${year} ${hours}:${minutes}`;
}