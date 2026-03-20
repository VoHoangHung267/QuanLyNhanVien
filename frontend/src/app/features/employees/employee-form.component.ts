import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { forkJoin } from 'rxjs';
import { EmployeeService } from '../../core/services/employee.service';
import { LanguageService } from '../../core/services/language.service';
import { CertificateService } from '../../core/services/certificate.service';
import { Language, Certificate } from '../../core/models';
 
@Component({
  selector: 'app-employee-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './employee-form.component.html',
  styleUrls: ['./employee-form.component.scss']
})
export class EmployeeFormComponent implements OnInit {
  form!: FormGroup;
  isEditMode = false;
  employeeId?: number;
  loading = false;
  submitting = false;
  errorMessage = '';
  languages: Language[] = [];
  certificates: Certificate[] = [];
  selectedLanguageIds: number[] = [];
  selectedCertificateIds: number[] = [];
 
  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private employeeService: EmployeeService,
    private languageService: LanguageService,
    private certificateService: CertificateService,
    private cdr: ChangeDetectorRef
  ) {}
 
  ngOnInit(): void {
    this.buildForm();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) { this.isEditMode = true; this.employeeId = +id; }
    this.loadCatalogs();
  }
 
  buildForm(): void {
    this.form = this.fb.group({
      name:    ['', [Validators.required, Validators.maxLength(100)]],
      dob:     ['', [Validators.required]],
      address: ['', [Validators.required]],
      phone:   ['', [Validators.required, Validators.pattern(/^[0-9]{10,15}$/)]]
    });
  }
 
  loadCatalogs(): void {
    this.loading = true;
    this.cdr.detectChanges(); // ← thêm

    forkJoin([
      this.languageService.getAll(),
      this.certificateService.getAll()
    ]).subscribe({
      next: ([langs, certs]) => {
        this.languages = langs.data;
        this.certificates = certs.data;
        this.cdr.detectChanges(); // ← thêm

        if (this.isEditMode) {
          this.loadEmployee();
        } else {
          this.loading = false;
          this.cdr.detectChanges(); // ← thêm
        }
      },
      error: () => {
        this.errorMessage = 'Không thể tải dữ liệu danh mục';
        this.loading = false;
        this.cdr.detectChanges(); // ← thêm
      }
    });
  }

  loadEmployee(): void {
    this.employeeService.getById(this.employeeId!).subscribe({
      next: (res) => {
        const e = res.data;
        this.form.patchValue({ name: e.name, dob: e.dob, address: e.address, phone: e.phone });
        this.selectedLanguageIds = e.languages.map(l => l.id);
        this.selectedCertificateIds = e.certificates.map(c => c.id);
        this.loading = false;
        this.cdr.detectChanges(); // ← thêm
      },
      error: () => {
        this.errorMessage = 'Không tìm thấy nhân viên';
        this.loading = false;
        this.cdr.detectChanges(); // ← thêm
      }
    });
  }

  toggleLanguage(id: number): void {
    const idx = this.selectedLanguageIds.indexOf(id);
    idx === -1 ? this.selectedLanguageIds.push(id) : this.selectedLanguageIds.splice(idx, 1);
  }

  toggleCertificate(id: number): void {
    const idx = this.selectedCertificateIds.indexOf(id);
    idx === -1 ? this.selectedCertificateIds.push(id) : this.selectedCertificateIds.splice(idx, 1);
  }

  isLangSelected(id: number): boolean { return this.selectedLanguageIds.includes(id); }
  isCertSelected(id: number): boolean { return this.selectedCertificateIds.includes(id); }

  get f() { return this.form.controls; }

  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.submitting = true;
    this.errorMessage = '';

    const payload = {
      ...this.form.value,
      languageIds: this.selectedLanguageIds,
      certificateIds: this.selectedCertificateIds
    };

    const call = this.isEditMode
      ? this.employeeService.update(this.employeeId!, payload)
      : this.employeeService.create(payload);

    call.subscribe({
      next: () => this.router.navigate(['/employees']),
      error: (err) => {
        const errData = err.error;
        if (errData?.data && typeof errData.data === 'object') {
          this.errorMessage = Object.values(errData.data).join(', ');
        } else {
          this.errorMessage = errData?.message || 'Có lỗi xảy ra';
        }
        this.submitting = false;
        this.cdr.detectChanges(); // ← thêm
      }
    });
  }
}