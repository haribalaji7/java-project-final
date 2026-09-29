import api from './api';

export interface DonationRequest {
  donorId: number;
  donationDate: string;
  hospitalName?: string;
  doctorName?: string;
  unitsDonated?: number;
  remarks?: string;
}

export interface DonationResponse {
  id: number;
  donorId: number;
  donorName: string;
  bloodGroup: string;
  donationDate: string;
  nextEligibleDate: string;
  hospitalName?: string;
  doctorName?: string;
  unitsDonated?: number;
  remarks?: string;
  createdAt?: string;
}

export const donationApi = {
  record: async (data: DonationRequest) => (await api.post<DonationResponse>('/donations', data)).data,
  getHistory: async (donorId: number) => (await api.get<DonationResponse[]>(`/donations/donor/${donorId}`)).data,
  getLatest: async () => (await api.get<DonationResponse>('/donations/latest')).data,
  getBetweenDates: async (startDate: string, endDate: string) => 
    (await api.get<DonationResponse[]>('/donations', { params: { startDate, endDate } })).data,
  getAll: async () => (await api.get<DonationResponse[]>('/donations')).data,
};
