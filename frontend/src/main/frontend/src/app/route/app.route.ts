import {ModuleWithProviders}  from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {AppUsersComponent} from "../component/component.admin/users/users.component";
import {PageNotFoundComponent} from "../component/pagenotfound/pagenotfound.component";
import {HomeComponent} from "../component/home/home.component";
import {DoughnutChartComponent} from "../component/chart/doughnutchart/doughnutchart.component";
import {ScheduleChartComponent} from "../component/chart/schedulechart/schedulechart.component";
import {AppUsersAddComponent} from "../component/component.admin/users/user.add.component";
import {RolesComponent} from "../component/component.admin/role/role.component";
import {ReportComponent} from "../component/component.admin/report/report.component";
import {ReportsAddComponent} from "../component/component.admin/report/report.add.component";
import {FieldOfStudyComponent} from "../component/component.admin/fieldofstudy/fieldofstudy.component";
import {FieldOfStudyAddComponent} from "../component/component.admin/fieldofstudy/fieldofstudy.add.component";
import {SubjectComponent} from "../component/component.admin/subject/subject.component";
import {SubjectAddComponent} from "../component/component.admin/subject/subject.add.component";
import {LaboratoryComponent} from "../component/component.admin/laboratory/laboratory.component";
import {LaboratoryAddComponent} from "../component/component.admin/laboratory/laboratory.add.component";
import {UserReportComponent} from "../component/component.user/report/report.component";
import {UserReportsAddComponent} from "../component/component.user/report/report.add.component";
import {UserAccountComponent} from "../component/component.user/user.account/user.account.component";
import {UserLaboratoryComponent} from "../component/component.user/laboratory/laboratory.component";
import {UserLaboratoryDetailComponent} from "../component/component.user/laboratory/laboratory.detail.component";
import {UserSubjectComponent} from "../component/component.user/subject/subject.component";
import {UserSubjectDetailComponent} from "../component/component.user/subject/subject.detail.component";
import {UserFieldOfStudyDetailComponent} from "../component/component.user/fieldofstudy/fieldofstudy.detail.component";
import {UserFieldOfStudyComponent} from "../component/component.user/fieldofstudy/fieldofstudy.component";
import {ReviewerReportsDetailComponent} from "../component/component.reviewer/report/report.detail.component";
import {ReviewerReportComponent} from "../component/component.reviewer/report/report.component";
import {ReviewerLaboratoryAddComponent} from "../component/component.reviewer/laboratory/laboratory.add.component";
import {ReviewerLaboratoryComponent} from "../component/component.reviewer/laboratory/laboratory.component";
import {ReviewerFieldOfStudyComponent} from "../component/component.reviewer/fieldofstudy/fieldofstudy.component";
import {ReviewerFieldOfStudyAddComponent} from "../component/component.reviewer/fieldofstudy/fieldofstudy.add.component";

export const appRoutes: Routes = [
  //Admin routes
  { path: 'users', component: AppUsersComponent },
  { path: 'users/:id', component: AppUsersAddComponent },
  { path: 'reports', component: ReportComponent },
  { path: 'reports/:id', component: ReportsAddComponent },
  { path: 'fieldsOfStudies', component: FieldOfStudyComponent },
  { path: 'fieldsOfStudies/:id', component: FieldOfStudyAddComponent },
  { path: 'subjects', component: SubjectComponent },
  { path: 'subjects/:id', component: SubjectAddComponent },
  { path: 'laboratories', component: LaboratoryComponent },
  { path: 'laboratories/:id', component: LaboratoryAddComponent },

  //User routes
  { path: 'userReports', component: UserReportComponent },
  { path: 'userReports/:id', component: UserReportsAddComponent },
  { path: 'userAccount', component: UserAccountComponent },
  { path: 'userLaboratories', component: UserLaboratoryComponent },
  { path: 'userLaboratories/:id', component: UserLaboratoryDetailComponent },
  { path: 'userSubjects', component: UserSubjectComponent },
  { path: 'userSubjects/:id', component: UserSubjectDetailComponent },
  { path: 'userFieldsOfStudies', component: UserFieldOfStudyComponent },
  { path: 'userFieldsOfStudies/:id', component: UserFieldOfStudyDetailComponent },

  //Reviewer routes
  { path: 'reviewerReports', component: ReviewerReportComponent },
  { path: 'reviewerReports/:id', component: ReviewerReportsDetailComponent },
  { path: 'reviewerLaboratories', component: ReviewerLaboratoryComponent },
  { path: 'reviewerLaboratories/:id', component: ReviewerLaboratoryAddComponent },
  { path: 'reviewerSubjects', component: ReviewerLaboratoryComponent },
  { path: 'reviewerSubjects/:id', component: ReviewerLaboratoryAddComponent },
  { path: 'reviewerFieldsOfStudies', component: ReviewerFieldOfStudyComponent },
  { path: 'reviewerFieldsOfStudies/:id', component: ReviewerFieldOfStudyAddComponent },

  //Other routes
  { path: 'schedule', component: ScheduleChartComponent },
  { path: 'doughnut', component: DoughnutChartComponent },
  { path: 'roles', component: RolesComponent },
  { path: '', component: HomeComponent },
  { path: '**', component: PageNotFoundComponent },
];

export const routing: ModuleWithProviders = RouterModule.forRoot(appRoutes);
