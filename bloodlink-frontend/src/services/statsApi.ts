import api from './api';

export interface BloodGroupStats {
  bloodGroup: string;
  availableDonors: number;
  totalDonors: number;
}

export interface CountDTO {
  count: number;
}

export interface GroupCountDTO {
  group: string;
  count: number;
}

export interface StringValueDTO {
  value: string;
}

export interface StatsSummaryDTO {
  totalDonors: number;
  availableDonors: number;
  totalDonations: number;
  donationsThisMonth: number;
}

export const statsApi = {
  getBloodGroupStats: async () => (await api.get<BloodGroupStats[]>('/stats/blood-groups')).data,
  getSummary: async () => (await api.get<StatsSummaryDTO>('/stats/summary')).data,
  getTotalDonors: async () => (await api.get<CountDTO>('/stats/donors/total')).data,
  getAvailableDonors: async () => (await api.get<CountDTO>('/stats/donors/available')).data,
  getUnavailableDonors: async () => (await api.get<CountDTO>('/stats/donors/unavailable')).data,
  getDonorsPerBloodGroup: async () => (await api.get<GroupCountDTO[]>('/stats/donors/by-blood-group')).data,
  getDonorsPerCity: async () => (await api.get<GroupCountDTO[]>('/stats/cities')).data,
  getDonationsThisMonth: async () => (await api.get<CountDTO>('/stats/donations/this-month')).data,
  getMostRequestedBloodGroup: async () => (await api.get<StringValueDTO>('/stats/donations/most-requested-blood-group')).data,
};
