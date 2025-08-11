export interface TrainingSessionRequest {
    trainerId: number,
    dateTime: Date | string,
    trainingType: string,
    trainingLevel: string,
}

export interface TrainingSessionResponse {
    id: number,
    trainerFirstName: string,
    trainerLastName: string,
    dateTime: Date | string,
    trainingType: string,
    trainingLevel: string
}