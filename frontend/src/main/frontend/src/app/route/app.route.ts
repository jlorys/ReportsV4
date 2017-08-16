import {ModuleWithProviders}  from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {AppUsersComponent} from "../component/users/users.component";
import {PageNotFoundComponent} from "../component/pagenotfound/pagenotfound.component";
import {HomeComponent} from "../component/home/home.component";
import {DoughnutChartComponent} from "../component/chart/doughnutchart/doughnutchart.component";
import {ScheduleChartComponent} from "../component/chart/schedulechart/schedulechart.component";
import {AppUsersAddComponent} from "../component/users/user.add.component";

export const appRoutes: Routes = [
  { path: 'users', component: AppUsersComponent},
  { path: 'users/:id', component: AppUsersAddComponent},
  { path: 'schedule', component: ScheduleChartComponent },
  { path: 'doughnut', component: DoughnutChartComponent },
  { path: '', component: HomeComponent},
  { path: '**', component: PageNotFoundComponent },
];

export const routing: ModuleWithProviders = RouterModule.forRoot(appRoutes);
