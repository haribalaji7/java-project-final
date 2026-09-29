import api from './api';

export interface DonorRequest {
  name: string;
  phone: string;
  city: string;
  bloodGroup: string;
}

export interface DonorResponse {
  id: number;
  name: string;
  phone: string;
  city: string;
  bloodGroup: string;
  available: boolean;
  lastDonationDate: string | null;
}

export const donorApi = {
  register: async (data: DonorRequest) => (await api.post<DonorResponse>('/donors', data)).data,
  getAll: async () => (await api.get<DonorResponse[]>('/donors')).data,
  search: async (city: string, bloodGroup: string) => 
    (await api.get<DonorResponse[]>('/donors/search', { params: { city, bloodGroup } })).data,
  getById: async (id: number) => (await api.get<DonorResponse>(`/donors/${id}`)).data,
};
