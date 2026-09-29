import { useEffect, useState } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '../components/ui/card';
import { Skeleton } from '../components/ui/skeleton';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, PieChart, Pie, Cell, Legend } from 'recharts';
import { statsApi } from '../services/statsApi';
import type { BloodGroupStats, GroupCountDTO } from '../services/statsApi';

const COLORS = ['#e11d48', '#0ea5e9', '#10b981', '#f59e0b', '#8b5cf6', '#ec4899', '#64748b', '#f43f5e'];

export default function Statistics() {
  const [bgStats, setBgStats] = useState<BloodGroupStats[]>([]);
  const [cityStats, setCityStats] = useState<GroupCountDTO[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [bg, city] = await Promise.all([
          statsApi.getBloodGroupStats(),
          statsApi.getDonorsPerCity(),
        ]);
        setBgStats(bg);
        setCityStats(city);
      } catch (error) {
        // Error handled by global interceptor
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  return (
    <div className="space-y-6 animate-in fade-in duration-500">
      <h1 className="text-3xl font-bold tracking-tight">Advanced Statistics</h1>
      
      <div className="grid gap-6 md:grid-cols-2">
        <Card className="glass">
          <CardHeader>
            <CardTitle>Donors by Blood Group</CardTitle>
          </CardHeader>
          <CardContent>
            {loading ? (
              <div className="h-[400px] w-full flex items-end justify-around pb-10">
                 {[40, 70, 30, 90, 50, 20, 80, 60].map((h, i) => (
                  <Skeleton key={i} className="w-8 rounded-t-md" style={{ height: `${h}%` }} />
                ))}
              </div>
            ) : (
              <div className="h-[400px]">
                <ResponsiveContainer width="100%" height="100%">
                  <BarChart data={bgStats} margin={{ top: 20, right: 30, left: 0, bottom: 5 }}>
                    <XAxis dataKey="bloodGroup" stroke="#888888" fontSize={12} tickLine={false} axisLine={false} />
                    <YAxis stroke="#888888" fontSize={12} tickLine={false} axisLine={false} />
                    <Tooltip cursor={{fill: 'transparent'}} contentStyle={{ borderRadius: '8px', border: 'none', boxShadow: '0 4px 6px -1px rgb(0 0 0 / 0.1)' }} />
                    <Bar dataKey="totalDonors" name="Total Donors" fill="#e11d48" radius={[4, 4, 0, 0]} animationDuration={1500} />
                    <Bar dataKey="availableDonors" name="Available Donors" fill="#10b981" radius={[4, 4, 0, 0]} animationDuration={1500} />
                    <Legend wrapperStyle={{ paddingTop: '20px' }} />
                  </BarChart>
                </ResponsiveContainer>
              </div>
            )}
          </CardContent>
        </Card>

        <Card className="glass">
          <CardHeader>
            <CardTitle>Donors by City</CardTitle>
          </CardHeader>
          <CardContent>
            {loading ? (
              <div className="h-[400px] flex items-center justify-center">
                 <Skeleton className="h-[250px] w-[250px] rounded-full" />
              </div>
            ) : (
              <div className="h-[400px]">
                <ResponsiveContainer width="100%" height="100%">
                  <PieChart>
                    <Pie
                      data={cityStats}
                      cx="50%"
                      cy="50%"
                      labelLine={false}
                      outerRadius={140}
                      fill="#8884d8"
                      dataKey="count"
                      nameKey="group"
                      animationDuration={1500}
                      label={({ name, percent }) => `${name} ${((percent || 0) * 100).toFixed(0)}%`}
                    >
                      {cityStats.map((_, index) => (
                        <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                      ))}
                    </Pie>
                    <Tooltip contentStyle={{ borderRadius: '8px', border: 'none', boxShadow: '0 4px 6px -1px rgb(0 0 0 / 0.1)' }} />
                    <Legend wrapperStyle={{ paddingTop: '20px' }} />
                  </PieChart>
                </ResponsiveContainer>
              </div>
            )}
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
