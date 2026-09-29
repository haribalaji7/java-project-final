import { useState, useEffect, useMemo } from 'react';
import { Card, CardContent } from '../components/ui/card';
import { Input } from '../components/ui/input';
import { Button } from '../components/ui/button';
import { Label } from '../components/ui/label';
import { Skeleton } from '../components/ui/skeleton';
import { donorApi } from '../services/donorApi';
import type { DonorResponse } from '../services/donorApi';
import { Search, MapPin, Phone, Droplet, UserX, Download, ArrowUpDown, ChevronLeft, ChevronRight } from 'lucide-react';
import toast from 'react-hot-toast';

const bloodGroups = ['All', 'A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'];

export default function SearchDonors() {
  const [searchTerm, setSearchTerm] = useState('');
  const [bloodGroup, setBloodGroup] = useState('All');
  const [donors, setDonors] = useState<DonorResponse[]>([]);
  const [loading, setLoading] = useState(true);
  
  // Sorting state
  const [sortField, setSortField] = useState<'name' | 'city' | 'available'>('available');
  const [sortDirection, setSortDirection] = useState<'asc' | 'desc'>('desc');

  // Pagination state
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 6;

  useEffect(() => {
    const fetchDonors = async () => {
      try {
        const data = await donorApi.getAll();
        setDonors(data);
      } catch (error) {
        // Error handled by global interceptor
      } finally {
        setLoading(false);
      }
    };
    fetchDonors();
  }, []);

  // Filter, Sort, and Paginate locally
  const filteredDonors = useMemo(() => {
    let result = donors;

    if (searchTerm) {
      const lower = searchTerm.toLowerCase();
      result = result.filter(d => 
        d.name.toLowerCase().includes(lower) || 
        d.city.toLowerCase().includes(lower) ||
        d.phone.includes(lower)
      );
    }

    if (bloodGroup !== 'All') {
      result = result.filter(d => d.bloodGroup === bloodGroup);
    }

    result.sort((a, b) => {
      if (sortField === 'available') {
        const valA = a.available ? 1 : 0;
        const valB = b.available ? 1 : 0;
        return sortDirection === 'asc' ? valA - valB : valB - valA;
      }
      const valA = String(a[sortField]).toLowerCase();
      const valB = String(b[sortField]).toLowerCase();
      if (valA < valB) return sortDirection === 'asc' ? -1 : 1;
      if (valA > valB) return sortDirection === 'asc' ? 1 : -1;
      return 0;
    });

    return result;
  }, [donors, searchTerm, bloodGroup, sortField, sortDirection]);

  const totalPages = Math.ceil(filteredDonors.length / itemsPerPage);
  const paginatedDonors = filteredDonors.slice((currentPage - 1) * itemsPerPage, currentPage * itemsPerPage);

  const toggleSort = (field: 'name' | 'city' | 'available') => {
    if (sortField === field) {
      setSortDirection(sortDirection === 'asc' ? 'desc' : 'asc');
    } else {
      setSortField(field);
      setSortDirection('asc');
    }
  };

  const exportToCSV = () => {
    if (filteredDonors.length === 0) return;
    const headers = ['ID', 'Name', 'Phone', 'City', 'Blood Group', 'Available', 'Last Donation'];
    const csvRows = filteredDonors.map(d => 
      [d.id, d.name, d.phone, d.city, d.bloodGroup, d.available ? 'Yes' : 'No', d.lastDonationDate || 'N/A'].join(',')
    );
    const csvContent = [headers.join(','), ...csvRows].join('\n');
    
    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', 'bloodlink_donors.csv');
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    toast.success('Exported to CSV');
  };

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-end justify-between gap-4">
        <div className="space-y-1">
          <h1 className="text-3xl font-bold tracking-tight">Donor Registry</h1>
          <p className="text-muted-foreground">Search, filter, and manage donors across all cities.</p>
        </div>
        <Button variant="outline" onClick={exportToCSV} disabled={filteredDonors.length === 0 || loading}>
          <Download className="mr-2 h-4 w-4" />
          Export CSV
        </Button>
      </div>

      <Card className="glass">
        <CardContent className="p-6">
          <div className="flex flex-col md:flex-row gap-4">
            <div className="space-y-2 flex-1">
              <Label htmlFor="search-term">Search by Name, City, or Phone</Label>
              <div className="relative">
                <Search className="absolute left-3 top-2.5 h-4 w-4 text-muted-foreground" />
                <Input 
                  id="search-term" 
                  placeholder="E.g. John Doe, New York..." 
                  className="pl-9"
                  value={searchTerm}
                  onChange={(e) => {
                    setSearchTerm(e.target.value);
                    setCurrentPage(1);
                  }}
                />
              </div>
            </div>
            <div className="space-y-2 md:w-64 shrink-0">
              <Label htmlFor="search-bg">Blood Group Filter</Label>
              <select 
                id="search-bg" 
                className="flex h-9 w-full rounded-md border border-input bg-background px-3 py-1 text-sm shadow-sm transition-colors focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring"
                value={bloodGroup}
                onChange={(e) => {
                  setBloodGroup(e.target.value);
                  setCurrentPage(1);
                }}
              >
                {bloodGroups.map(bg => (
                  <option key={bg} value={bg}>{bg}</option>
                ))}
              </select>
            </div>
          </div>

          {/* Quick Sort Actions */}
          <div className="flex flex-wrap gap-2 mt-4 pt-4 border-t border-border">
            <span className="text-sm font-medium text-muted-foreground self-center mr-2">Sort by:</span>
            <Button variant="ghost" size="sm" onClick={() => toggleSort('name')} className={sortField === 'name' ? 'bg-primary/10 text-primary' : ''}>
              Name <ArrowUpDown className="ml-2 h-3 w-3" />
            </Button>
            <Button variant="ghost" size="sm" onClick={() => toggleSort('city')} className={sortField === 'city' ? 'bg-primary/10 text-primary' : ''}>
              City <ArrowUpDown className="ml-2 h-3 w-3" />
            </Button>
            <Button variant="ghost" size="sm" onClick={() => toggleSort('available')} className={sortField === 'available' ? 'bg-primary/10 text-primary' : ''}>
              Availability <ArrowUpDown className="ml-2 h-3 w-3" />
            </Button>
          </div>
        </CardContent>
      </Card>

      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
        {loading ? (
          Array.from({ length: 6 }).map((_, i) => (
            <Card key={i} className="glass overflow-hidden">
              <Skeleton className="h-2 w-full rounded-none" />
              <CardContent className="p-6">
                <div className="flex justify-between items-start mb-4">
                  <div className="space-y-2 w-2/3">
                    <Skeleton className="h-6 w-3/4" />
                    <Skeleton className="h-4 w-1/2" />
                  </div>
                  <Skeleton className="h-10 w-10 rounded-full" />
                </div>
                <div className="space-y-3 mt-4">
                  <Skeleton className="h-4 w-1/2" />
                  <Skeleton className="h-4 w-3/4" />
                </div>
              </CardContent>
            </Card>
          ))
        ) : filteredDonors.length === 0 ? (
          <div className="col-span-full flex flex-col items-center justify-center py-16 text-muted-foreground bg-card/50 rounded-xl border border-dashed border-border">
            <UserX className="h-16 w-16 mb-4 text-muted-foreground/50" />
            <h3 className="text-lg font-medium text-foreground">No donors found</h3>
            <p className="text-sm mt-1">Try adjusting your search criteria or clearing filters.</p>
            <Button variant="outline" className="mt-4" onClick={() => { setSearchTerm(''); setBloodGroup('All'); }}>
              Clear Filters
            </Button>
          </div>
        ) : (
          paginatedDonors.map(donor => (
            <Card key={donor.id} className="glass overflow-hidden group hover:shadow-md transition-shadow">
              <div className={`h-2 w-full transition-colors ${donor.available ? 'bg-green-500' : 'bg-orange-500'}`} />
              <CardContent className="p-6">
                <div className="flex justify-between items-start mb-4">
                  <div>
                    <h3 className="font-bold text-lg leading-tight group-hover:text-primary transition-colors">{donor.name}</h3>
                    <div className="flex items-center text-sm text-muted-foreground mt-1.5">
                      <MapPin className="h-3.5 w-3.5 mr-1.5 shrink-0" />
                      <span className="truncate max-w-[150px]">{donor.city}</span>
                    </div>
                  </div>
                  <div className="flex items-center justify-center h-11 w-11 shrink-0 rounded-full bg-primary/10 text-primary font-bold shadow-sm">
                    {donor.bloodGroup}
                  </div>
                </div>
                
                <div className="space-y-3 pt-2">
                  <div className="flex items-center text-sm">
                    <Phone className="h-4 w-4 mr-2 text-muted-foreground" />
                    {donor.phone}
                  </div>
                  <div className="flex items-center text-sm">
                    <Droplet className="h-4 w-4 mr-2 text-muted-foreground" />
                    {donor.available ? (
                      <span className="text-green-600 dark:text-green-500 font-medium bg-green-500/10 px-2 py-0.5 rounded-sm">Available</span>
                    ) : (
                      <span className="text-orange-600 dark:text-orange-400 font-medium bg-orange-500/10 px-2 py-0.5 rounded-sm">
                        Cooldown (until {donor.lastDonationDate})
                      </span>
                    )}
                  </div>
                </div>
              </CardContent>
            </Card>
          ))
        )}
      </div>

      {/* Pagination Controls */}
      {!loading && filteredDonors.length > itemsPerPage && (
        <div className="flex items-center justify-center gap-2 mt-6 pb-6">
          <Button 
            variant="outline" 
            size="icon"
            onClick={() => setCurrentPage(p => Math.max(1, p - 1))}
            disabled={currentPage === 1}
          >
            <ChevronLeft className="h-4 w-4" />
          </Button>
          <div className="text-sm font-medium text-muted-foreground">
            Page {currentPage} of {totalPages}
          </div>
          <Button 
            variant="outline" 
            size="icon"
            onClick={() => setCurrentPage(p => Math.min(totalPages, p + 1))}
            disabled={currentPage === totalPages}
          >
            <ChevronRight className="h-4 w-4" />
          </Button>
        </div>
      )}
    </div>
  );
}
