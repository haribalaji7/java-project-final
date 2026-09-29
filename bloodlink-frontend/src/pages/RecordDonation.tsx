import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '../components/ui/card';
import { Input } from '../components/ui/input';
import { Label } from '../components/ui/label';
import { Button } from '../components/ui/button';
import { donationApi } from '../services/donationApi';
import toast from 'react-hot-toast';

const formSchema = z.object({
  donorId: z.coerce.number().min(1, 'Donor ID is required'),
  donationDate: z.string().min(1, 'Donation date is required'),
  hospitalName: z.string().optional(),
  doctorName: z.string().optional(),
  unitsDonated: z.coerce.number().optional(),
  remarks: z.string().optional(),
});

type FormData = z.infer<typeof formSchema>;

export default function RecordDonation() {
  const [loading, setLoading] = useState(false);
  
  const { register, handleSubmit, reset, formState: { errors } } = useForm<FormData>({
    resolver: zodResolver(formSchema) as any,
    defaultValues: {
      donationDate: new Date().toISOString().split('T')[0],
      unitsDonated: 1,
    }
  });

  const onSubmit = async (data: FormData) => {
    setLoading(true);
    try {
      await donationApi.record(data);
      toast.success('Donation recorded successfully!');
      reset({
        donationDate: new Date().toISOString().split('T')[0],
        unitsDonated: 1,
      });
    } catch (error: any) {
      // Error handled by global interceptor
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-2xl mx-auto">
      <Card className="glass">
        <CardHeader>
          <CardTitle className="text-2xl">Record Blood Donation</CardTitle>
          <CardDescription>Log a new donation to update donor availability automatically.</CardDescription>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label htmlFor="donorId">Donor ID</Label>
                <Input id="donorId" type="number" placeholder="123" {...register('donorId')} />
                {errors.donorId && <p className="text-sm text-destructive">{errors.donorId.message}</p>}
              </div>
              <div className="space-y-2">
                <Label htmlFor="donationDate">Donation Date</Label>
                <Input id="donationDate" type="date" {...register('donationDate')} />
                {errors.donationDate && <p className="text-sm text-destructive">{errors.donationDate.message}</p>}
              </div>
            </div>

            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label htmlFor="hospitalName">Hospital Name</Label>
                <Input id="hospitalName" placeholder="City General Hospital" {...register('hospitalName')} />
              </div>
              <div className="space-y-2">
                <Label htmlFor="doctorName">Doctor Name</Label>
                <Input id="doctorName" placeholder="Dr. Smith" {...register('doctorName')} />
              </div>
            </div>

            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label htmlFor="unitsDonated">Units Donated</Label>
                <Input id="unitsDonated" type="number" min="1" {...register('unitsDonated')} />
              </div>
              <div className="space-y-2">
                <Label htmlFor="remarks">Notes / Remarks</Label>
                <Input id="remarks" placeholder="Any complications?" {...register('remarks')} />
              </div>
            </div>

            <Button type="submit" className="w-full" disabled={loading}>
              {loading ? 'Recording...' : 'Record Donation'}
            </Button>
          </form>
        </CardContent>
      </Card>
    </div>
  );
}
