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

export const appRoutes: Routes = [
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
  { path: 'schedule', component: ScheduleChartComponent },
  { path: 'doughnut', component: DoughnutChartComponent },
  { path: 'roles', component: RolesComponent },
  { path: '', component: HomeComponent },
  { path: '**', component: PageNotFoundComponent },
];

export const routing: ModuleWithProviders = RouterModule.forRoot(appRoutes);
